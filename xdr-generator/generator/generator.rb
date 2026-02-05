require 'set'

class Generator < Xdrgen::Generators::Base
  AST = Xdrgen::AST

  def generate
    constants_container = Set[]
    render_lib
    render_definitions(@top, constants_container)
    render_constants constants_container
  end

  def render_lib
    template = IO.read(__dir__ + "/templates/XdrDataInputStream.erb")
    result = ERB.new(template).result binding
    @output.write  "XdrDataInputStream.java", result

    template = IO.read(__dir__ + "/templates/XdrDataOutputStream.erb")
    result = ERB.new(template).result binding
    @output.write  "XdrDataOutputStream.java", result

    template = IO.read(__dir__ + "/templates/XdrElement.erb")
    result = ERB.new(template).result binding
    @output.write  "XdrElement.java", result

    template = IO.read(__dir__ + "/templates/XdrString.erb")
    result = ERB.new(template).result binding
    @output.write  "XdrString.java", result

    template = IO.read(__dir__ + "/templates/XdrUnsignedHyperInteger.erb")
    result = ERB.new(template).result binding
    @output.write  "XdrUnsignedHyperInteger.java", result

    template = IO.read(__dir__ + "/templates/XdrUnsignedInteger.erb")
    result = ERB.new(template).result binding
    @output.write  "XdrUnsignedInteger.java", result
  end

  def render_definitions(node, constants_container)
    node.namespaces.each{|n| render_definitions n, constants_container }
    node.definitions.each { |defn| render_definition(defn, constants_container) }
  end

  def add_imports_for_definition(defn, imports)
    imports.add("org.stellar.sdk.Base64Factory")
    imports.add("java.io.ByteArrayInputStream")
    imports.add("java.io.ByteArrayOutputStream")

    case defn
    when AST::Definitions::Struct, AST::Definitions::Union
      imports.add("lombok.Data")
      imports.add("lombok.NoArgsConstructor")
      imports.add("lombok.AllArgsConstructor")
      imports.add("lombok.Builder")
    when AST::Definitions::Typedef
      imports.add("lombok.Data")
      imports.add("lombok.NoArgsConstructor")
      imports.add("lombok.AllArgsConstructor")
    end

    if defn.respond_to? :nested_definitions
      defn.nested_definitions.each{ |child_defn| add_imports_for_definition(child_defn, imports) }
    end
  end

  def render_definition(defn, constants_container)
    imports = Set[]
    add_imports_for_definition(defn, imports)

    case defn
    when AST::Definitions::Struct ;
      render_element defn, imports, defn do |out|
        render_struct defn, out
        render_nested_definitions defn, out
      end
    when AST::Definitions::Enum ;
      render_element defn, imports, defn do |out|
        render_enum defn, out
      end
    when AST::Definitions::Union ;
      render_element defn, imports, defn do |out|
        render_union defn, out
        render_nested_definitions defn, out
      end
    when AST::Definitions::Typedef ;
      render_element defn, imports, defn do |out|
        render_typedef defn, out
      end
    when AST::Definitions::Const ;
      const_name = defn.name
      const_value = defn.value
      constants_container.add([const_name, const_value])
    end
  end

  def render_nested_definitions(defn, out, post_name="implements XdrElement")
    return unless defn.respond_to? :nested_definitions
    defn.nested_definitions.each{|ndefn|
      render_source_comment out, ndefn
      case ndefn
      when AST::Definitions::Struct ;
        name = name ndefn
        out.puts "@Data"
        out.puts "@NoArgsConstructor"
        out.puts "@AllArgsConstructor"
        out.puts "@Builder(toBuilder = true)"
        out.puts "public static class #{name} #{post_name} {"
        out.indent do
          render_struct ndefn, out
          render_nested_definitions ndefn , out
        end
        out.puts "}"
      when AST::Definitions::Enum ;
        name = name ndefn
        out.puts "public static enum #{name} #{post_name} {"
        out.indent do
          render_enum ndefn, out
        end
        out.puts "}"
      when AST::Definitions::Union ;
        name = name ndefn
        out.puts "@Data"
        out.puts "@NoArgsConstructor"
        out.puts "@AllArgsConstructor"
        out.puts "@Builder(toBuilder = true)"
        out.puts "public static class #{name} #{post_name} {"
        out.indent do
          render_union ndefn, out
          render_nested_definitions ndefn, out
        end
        out.puts "}"
      when AST::Definitions::Typedef ;
        name = name ndefn
        out.puts "@Data"
        out.puts "@NoArgsConstructor"
        out.puts "@AllArgsConstructor"
        out.puts "public static class #{name} #{post_name} {"
        out.indent do
          render_typedef ndefn, out
        end
        out.puts "}"
      end
    }
  end

  def render_element(defn, imports, element, post_name="implements XdrElement")
    path = element.name.camelize + ".java"
    name = name_string element.name
    out  = @output.open(path)
    render_top_matter out
    imports.each do |import|
      out.puts "import #{import};"
    end
    out.puts "\n"
    render_source_comment out, element
    case defn
    when AST::Definitions::Struct, AST::Definitions::Union
      out.puts "@Data"
      out.puts "@NoArgsConstructor"
      out.puts "@AllArgsConstructor"
      out.puts "@Builder(toBuilder = true)"
      out.puts "public class #{name} #{post_name} {"
    when AST::Definitions::Enum
      out.puts "public enum #{name} #{post_name} {"
    when AST::Definitions::Typedef
      out.puts "@Data"
      out.puts "@NoArgsConstructor"
      out.puts "@AllArgsConstructor"
      out.puts "public class #{name} #{post_name} {"
    end
    out.indent do
      yield out
      out.unbreak
    end
    out.puts "}"
  end

  def render_constants(constants_container)
    out = @output.open("Constants.java")
    render_top_matter out
    out.puts "public final class Constants {"
    out.indent do
      out.puts "private Constants() {}"
      # Sort constants by name for consistent output
      constants_container.sort_by { |const_name, _| const_name }.each do |const_name, const_value|
        out.puts "public static final int #{const_name} = #{const_value};"
      end
    end
    out.puts "}"
  end

  def render_enum(enum, out)
    out.balance_after /,[\s]*/ do
      enum.members.each_with_index do |em, index|
        out.puts "#{em.name}(#{em.value})#{index == enum.members.size - 1 ? ';' : ','}"
      end
    end
    out.break
    out.puts <<-EOS.strip_heredoc
    private final int value;

    #{name_string enum.name}(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static #{name_string enum.name} decode(XdrDataInputStream stream, int maxDepth) throws IOException {
      // maxDepth is intentionally not checked - enums are leaf types with no recursive decoding
      int value = stream.readInt();
      switch (value) {
    EOS
    out.indent 2 do
      enum.members.each do |em|
        out.puts "case #{em.value}: return #{em.name};"
      end
    end
    out.puts <<-EOS.strip_heredoc
        default:
          throw new IllegalArgumentException("Unknown enum value: " + value);
      }
    }

    public static #{name_string enum.name} decode(XdrDataInputStream stream) throws IOException {
      return decode(stream, XdrDataInputStream.DEFAULT_MAX_DEPTH);
    }

    public void encode(XdrDataOutputStream stream) throws IOException {
      stream.writeInt(value);
    }
    EOS
    render_base64((name_string enum.name), out)
    out.break
  end

  def render_struct(struct, out)
    struct.members.each do |m|
      out.puts "private #{decl_string(m.declaration)} #{m.name};"
    end

    out.puts "public void encode(XdrDataOutputStream stream) throws IOException{"
    struct.members.each do |m|
      out.indent do
        encode_member m, out
      end
    end
    out.puts "}"

    # decode with maxDepth parameter
    out.puts <<-EOS.strip_heredoc
      public static #{name struct} decode(XdrDataInputStream stream, int maxDepth) throws IOException {
        if (maxDepth <= 0) {
          throw new IOException("Maximum decoding depth reached");
        }
        maxDepth -= 1;
        #{name struct} decoded#{name struct} = new #{name struct}();
    EOS
    struct.members.each do |m|
      out.indent do
        decode_member "decoded#{name struct}", m, out, "maxDepth"
      end
    end
    out.indent do
      out.puts "return decoded#{name struct};"
    end
    out.puts "}"

    # decode without maxDepth parameter (uses default)
    out.puts <<-EOS.strip_heredoc
      public static #{name struct} decode(XdrDataInputStream stream) throws IOException {
        return decode(stream, XdrDataInputStream.DEFAULT_MAX_DEPTH);
      }
    EOS

    render_base64((name struct), out)
    out.break
  end

  def render_typedef(typedef, out)
    out.puts "private #{decl_string typedef.declaration} #{typedef.name};"
    out.puts "public void encode(XdrDataOutputStream stream) throws IOException {"
    out.indent do
      encode_member typedef, out
    end
    out.puts "}"
    out.break

    # decode with maxDepth parameter
    out.puts <<-EOS.strip_heredoc
      public static #{name typedef} decode(XdrDataInputStream stream, int maxDepth) throws IOException {
        if (maxDepth <= 0) {
          throw new IOException("Maximum decoding depth reached");
        }
        maxDepth -= 1;
        #{name typedef} decoded#{name typedef} = new #{name typedef}();
    EOS
    out.indent do
      decode_member "decoded#{name typedef}", typedef, out, "maxDepth"
      out.puts "return decoded#{name typedef};"
    end
    out.puts "}"

    # decode without maxDepth parameter (uses default)
    out.puts <<-EOS.strip_heredoc
      public static #{name typedef} decode(XdrDataInputStream stream) throws IOException {
        return decode(stream, XdrDataInputStream.DEFAULT_MAX_DEPTH);
      }
    EOS
    out.break
    render_base64(typedef.name.camelize, out)
  end

  def render_union(union, out)
    out.puts "private #{type_string union.discriminant.type} discriminant;"
    union.arms.each do |arm|
      next if arm.void?
      out.puts "private #{decl_string(arm.declaration)} #{arm.name};"
    end
    out.break

    out.puts "public void encode(XdrDataOutputStream stream) throws IOException {"
    if union.discriminant.type.is_a?(AST::Typespecs::Int)
      out.puts "stream.writeInt(discriminant);"
    elsif type_string(union.discriminant.type) == "Uint32"
      # ugly workaround for compile error after generating source for AuthenticatedMessage in stellar-core
      out.puts "stream.writeInt(discriminant.getUint32().getNumber().intValue());"
    else
      out.puts "stream.writeInt(discriminant.getValue());"
    end
    if type_string(union.discriminant.type) == "Uint32"
      # ugly workaround for compile error after generating source for AuthenticatedMessage in stellar-core
      out.puts "switch (discriminant.getUint32().getNumber().intValue()) {"
    else
      out.puts "switch (discriminant) {"
    end
    union.arms.each do |arm|
      case arm
        when AST::Definitions::UnionDefaultArm ;
          out.puts "default:"
        else
          arm.cases.each do |kase|
            if kase.value.is_a?(AST::Identifier)
              if type_string(union.discriminant.type) == "Integer"
                member = union.resolved_case(kase)
                out.puts "case #{member.value}:"
              else
                out.puts "case #{kase.value.name}:"
              end
            else
              out.puts "case #{kase.value.value}:"
            end
          end
      end
      encode_member arm, out
      out.puts "break;"
    end
    out.puts "}\n}"

    # decode with maxDepth parameter
    out.puts "public static #{name union} decode(XdrDataInputStream stream, int maxDepth) throws IOException {"
    out.puts "if (maxDepth <= 0) {"
    out.puts "  throw new IOException(\"Maximum decoding depth reached\");"
    out.puts "}"
    out.puts "maxDepth -= 1;"
    out.puts "#{name union} decoded#{name union} = new #{name union}();"
    if union.discriminant.type.is_a?(AST::Typespecs::Int)
      out.puts "Integer discriminant = stream.readInt();"
    else
      out.puts "#{name union.discriminant.type} discriminant = #{name union.discriminant.type}.decode(stream, maxDepth);"
    end
    out.puts "decoded#{name union}.setDiscriminant(discriminant);"

    if type_string(union.discriminant.type) == "Uint32"
      # ugly workaround for compile error after generating source for AuthenticatedMessage in stellar-core
      out.puts "switch (decoded#{name union}.getDiscriminant().getUint32().getNumber().intValue()) {"
    else
      out.puts "switch (decoded#{name union}.getDiscriminant()) {"
    end

    has_default_arm = union.arms.any? { |arm| arm.is_a?(AST::Definitions::UnionDefaultArm) }
    union.arms.each do |arm|
      case arm
        when AST::Definitions::UnionDefaultArm ;
          out.puts "default:"
        else
          arm.cases.each do |kase|
            if kase.value.is_a?(AST::Identifier)
              if type_string(union.discriminant.type) == "Integer"
                member = union.resolved_case(kase)
                out.puts "case #{member.value}:"
              else
                out.puts "case #{kase.value.name}:"
              end
            else
              out.puts "case #{kase.value.value}:"
            end
          end
      end
      decode_member "decoded#{name union}", arm, out, "maxDepth"
      out.puts "break;"
    end
    unless has_default_arm
      out.puts "default:"
      out.puts "  throw new IOException(\"Unknown discriminant value: \" + discriminant);"
    end
    out.puts "}\n"
    out.indent do
      out.puts "return decoded#{name union};"
    end
    out.puts "}"

    # decode without maxDepth parameter (uses default)
    out.puts <<-EOS.strip_heredoc
      public static #{name union} decode(XdrDataInputStream stream) throws IOException {
        return decode(stream, XdrDataInputStream.DEFAULT_MAX_DEPTH);
      }
    EOS
    render_base64((name union), out)
    out.break
  end

  def render_top_matter(out)
    out.puts <<-EOS.strip_heredoc
      // Automatically generated by xdrgen
      // DO NOT EDIT or your changes may be overwritten

      package #{@namespace};

      import java.io.IOException;
    EOS
    out.break
  end

  def render_source_comment(out, defn)
    return if defn.is_a?(AST::Definitions::Namespace)

    out.puts "/**"
    out.puts " * #{name defn}'s original definition in the XDR file is:"
    out.puts " * <pre>"
    out.puts " * " + escape_html(defn.text_value).split("\n").join("\n * ")
    out.puts " * </pre>"
    out.puts " */"
  end

  def render_base64(return_type, out)
    out.puts <<-EOS.strip_heredoc
      public static #{return_type} fromXdrBase64(String xdr) throws IOException {
        byte[] bytes = Base64Factory.getInstance().decode(xdr);
        return fromXdrByteArray(bytes);
      }

      public static #{return_type} fromXdrByteArray(byte[] xdr) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
        XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
        xdrDataInputStream.setMaxInputLen(xdr.length);
        return decode(xdrDataInputStream);
      }
    EOS
  end

  def encode_member(member, out)
    case member.declaration
      when AST::Declarations::Void
        return
    end

    if member.type.sub_type == :optional
      out.puts "if (#{member.name} != null) {"
      out.puts "stream.writeInt(1);"
    end
    case member.declaration
    when AST::Declarations::Opaque ;
      out.puts "int #{member.name}Size = #{member.name}.length;"
      if member.declaration.fixed?
        out.puts "if (#{member.name}Size != #{convert_constant member.declaration.size}) {"
        out.puts "  throw new IOException(\"#{member.name} size \" + #{member.name}Size + \" does not match fixed size #{member.declaration.size}\");"
        out.puts "}"
      else
        max_size = member.declaration.resolved_size
        if max_size
          out.puts "if (#{member.name}Size > #{convert_constant max_size}) {"
          out.puts "  throw new IOException(\"#{member.name} size \" + #{member.name}Size + \" exceeds max size #{max_size}\");"
          out.puts "}"
        end
        out.puts "stream.writeInt(#{member.name}Size);"
      end
      out.puts <<-EOS.strip_heredoc
        stream.write(get#{member.name.slice(0,1).capitalize+member.name.slice(1..-1)}(), 0, #{member.name}Size);
      EOS
    when AST::Declarations::Array ;
      out.puts "int #{member.name}Size = get#{member.name.slice(0,1).capitalize+member.name.slice(1..-1)}().length;"
      if member.declaration.fixed?
        out.puts "if (#{member.name}Size != #{convert_constant member.declaration.size}) {"
        out.puts "  throw new IOException(\"#{member.name} size \" + #{member.name}Size + \" does not match fixed size #{member.declaration.size}\");"
        out.puts "}"
      else
        max_size = member.declaration.resolved_size
        if max_size
          out.puts "if (#{member.name}Size > #{convert_constant max_size}) {"
          out.puts "  throw new IOException(\"#{member.name} size \" + #{member.name}Size + \" exceeds max size #{max_size}\");"
          out.puts "}"
        end
        out.puts "stream.writeInt(#{member.name}Size);"
      end
      out.puts <<-EOS.strip_heredoc
        for (int i = 0; i < #{member.name}Size; i++) {
          #{encode_type member.declaration.type, "#{member.name}[i]"};
        }
      EOS
    when AST::Declarations::String ;
      max_size = member.declaration.resolved_size
      if max_size
        out.puts "int #{member.name}Size = #{member.name}.getBytes().length;"
        out.puts "if (#{member.name}Size > #{convert_constant max_size}) {"
        out.puts "  throw new IOException(\"#{member.name} size \" + #{member.name}Size + \" exceeds max size #{max_size}\");"
        out.puts "}"
      end
      out.puts "#{member.name}.encode(stream);"
    else
      out.puts "#{encode_type member.declaration.type, "#{member.name}"};"
    end
    if member.type.sub_type == :optional
      out.puts "} else {"
      out.puts "stream.writeInt(0);"
      out.puts "}"
    end
  end

  def encode_type(type, value)
    case type
    when AST::Typespecs::Int ;
      "stream.writeInt(#{value})"
    when AST::Typespecs::UnsignedInt ;
      "#{value}.encode(stream)"
    when AST::Typespecs::Hyper ;
      "stream.writeLong(#{value})"
    when AST::Typespecs::UnsignedHyper ;
      "#{value}.encode(stream)"
    when AST::Typespecs::Float ;
      "stream.writeFloat(#{value})"
    when AST::Typespecs::Double ;
      "stream.writeDouble(#{value})"
    when AST::Typespecs::Quadruple ;
      raise "cannot render quadruple in java"
    when AST::Typespecs::Bool ;
      "stream.writeInt(#{value} ? 1 : 0)"
    when AST::Typespecs::String ;
      "#{value}.encode(stream)"
    when AST::Typespecs::Simple ;
      "#{value}.encode(stream)"
    when AST::Concerns::NestedDefinition ;
      "#{value}.encode(stream)"
    else
      raise "Unknown typespec: #{type.class.name}"
    end
  end

  def decode_member(value, member, out, depth_var = nil)
    case member.declaration
    when AST::Declarations::Void ;
      return
    end
    if member.type.sub_type == :optional
      out.puts <<-EOS.strip_heredoc
        boolean #{member.name}Present = stream.readXdrBoolean();
        if (#{member.name}Present) {
      EOS
    end
    case member.declaration
    when AST::Declarations::Opaque ;
      if (member.declaration.fixed?)
        out.puts "int #{member.name}Size = #{convert_constant member.declaration.size};"
      else
        out.puts "int #{member.name}Size = stream.readInt();"
        # Add size validation for variable-length opaque
        out.puts "if (#{member.name}Size < 0) {"
        out.puts "  throw new IOException(\"#{member.name} size \" + #{member.name}Size + \" is negative\");"
        out.puts "}"
        max_size = member.declaration.resolved_size
        if max_size
          out.puts "if (#{member.name}Size > #{convert_constant max_size}) {"
          out.puts "  throw new IOException(\"#{member.name} size \" + #{member.name}Size + \" exceeds max size #{max_size}\");"
          out.puts "}"
        end
        # Add input length check to prevent DoS
        out.puts "int #{member.name}RemainingInputLen = stream.getRemainingInputLen();"
        out.puts "if (#{member.name}RemainingInputLen >= 0 && #{member.name}RemainingInputLen < #{member.name}Size) {"
        out.puts "  throw new IOException(\"#{member.name} size \" + #{member.name}Size + \" exceeds remaining input length \" + #{member.name}RemainingInputLen);"
        out.puts "}"
      end
      out.puts <<-EOS.strip_heredoc
        #{value}.#{member.name} = new byte[#{member.name}Size];
        stream.readPaddedData(#{value}.#{member.name}, 0, #{member.name}Size);
      EOS
    when AST::Declarations::Array ;
      if (member.declaration.fixed?)
        out.puts "int #{member.name}Size = #{convert_constant member.declaration.size};"
      else
        out.puts "int #{member.name}Size = stream.readInt();"
        # Add size validation for variable-length array
        out.puts "if (#{member.name}Size < 0) {"
        out.puts "  throw new IOException(\"#{member.name} size \" + #{member.name}Size + \" is negative\");"
        out.puts "}"
        max_size = member.declaration.resolved_size
        if max_size
          out.puts "if (#{member.name}Size > #{convert_constant max_size}) {"
          out.puts "  throw new IOException(\"#{member.name} size \" + #{member.name}Size + \" exceeds max size #{max_size}\");"
          out.puts "}"
        end
        # Add input length check to prevent DoS
        out.puts "int #{member.name}RemainingInputLen = stream.getRemainingInputLen();"
        out.puts "if (#{member.name}RemainingInputLen >= 0 && #{member.name}RemainingInputLen < #{member.name}Size) {"
        out.puts "  throw new IOException(\"#{member.name} size \" + #{member.name}Size + \" exceeds remaining input length \" + #{member.name}RemainingInputLen);"
        out.puts "}"
      end
      out.puts <<-EOS.strip_heredoc
        #{value}.#{member.name} = new #{type_string member.type}[#{member.name}Size];
        for (int i = 0; i < #{member.name}Size; i++) {
          #{value}.#{member.name}[i] = #{decode_type member.declaration, depth_var};
        }
      EOS
    else
      out.puts "#{value}.#{member.name} = #{decode_type member.declaration, depth_var};"
    end
    if member.type.sub_type == :optional
      out.puts "}"
    end
  end

  def decode_type(decl, depth_var = nil)
    case decl.type
    when AST::Typespecs::Int ;
      "stream.readInt()"
    when AST::Typespecs::UnsignedInt ;
      depth_var ? "XdrUnsignedInteger.decode(stream, #{depth_var})" : "XdrUnsignedInteger.decode(stream)"
    when AST::Typespecs::Hyper ;
      "stream.readLong()"
    when AST::Typespecs::UnsignedHyper ;
      depth_var ? "XdrUnsignedHyperInteger.decode(stream, #{depth_var})" : "XdrUnsignedHyperInteger.decode(stream)"
    when AST::Typespecs::Float ;
      "stream.readFloat()"
    when AST::Typespecs::Double ;
      "stream.readDouble()"
    when AST::Typespecs::Quadruple ;
      raise "cannot render quadruple in java"
    when AST::Typespecs::Bool ;
      "stream.readXdrBoolean()"
    when AST::Typespecs::String ;
      depth_var ? "XdrString.decode(stream, #{depth_var}, #{(convert_constant decl.size) || 'Integer.MAX_VALUE'})" : "XdrString.decode(stream, #{(convert_constant decl.size) || 'Integer.MAX_VALUE'})"
    when AST::Typespecs::Simple ;
      depth_var ? "#{name decl.type.resolved_type}.decode(stream, #{depth_var})" : "#{name decl.type.resolved_type}.decode(stream)"
    when AST::Concerns::NestedDefinition ;
      depth_var ? "#{name decl.type}.decode(stream, #{depth_var})" : "#{name decl.type}.decode(stream)"
    else
      raise "Unknown typespec: #{decl.type.class.name}"
    end
  end

  def decl_string(decl)
    case decl
    when AST::Declarations::Opaque ;
      "byte[]"
    when AST::Declarations::String ;
      "XdrString"
    when AST::Declarations::Array ;
      "#{type_string decl.type}[]"
    when AST::Declarations::Optional ;
      "#{type_string(decl.type)}"
    when AST::Declarations::Simple ;
      type_string(decl.type)
    else
      raise "Unknown declaration type: #{decl.class.name}"
    end
  end

  def is_decl_array(decl)
    case decl
    when AST::Declarations::Opaque ;
      true
    when AST::Declarations::Array ;
      true
    when AST::Declarations::Optional ;
      is_type_array(decl.type)
    when AST::Declarations::Simple ;
    is_type_array(decl.type)
    else
      false
    end
  end

  def is_type_array(type)
    case type
    when AST::Typespecs::Opaque ;
      true
    else
      false
    end
  end

  def type_string(type)
    case type
    when AST::Typespecs::Int ;
      "Integer"
    when AST::Typespecs::UnsignedInt ;
      "XdrUnsignedInteger"
    when AST::Typespecs::Hyper ;
      "Long"
    when AST::Typespecs::UnsignedHyper ;
      "XdrUnsignedHyperInteger"
    when AST::Typespecs::Float ;
      "Float"
    when AST::Typespecs::Double ;
      "Double"
    when AST::Typespecs::Quadruple ;
      raise "cannot render quadruple in java"
    when AST::Typespecs::Bool ;
      "Boolean"
    when AST::Typespecs::Opaque ;
      "Byte[#{convert_constant type.size}]"
    when AST::Typespecs::String ;
      "XdrString"
    when AST::Typespecs::Simple ;
      name type.resolved_type
    when AST::Concerns::NestedDefinition ;
      name type
    else
      raise "Unknown typespec: #{type.class.name}"
    end
  end

  def name(named)
    parent = name named.parent_defn if named.is_a?(AST::Concerns::NestedDefinition)
    result = named.name.camelize

    "#{parent}#{result}"
  end

  def name_string(name)
    name.camelize
  end

  def escape_html(value)
    value.to_s
         .gsub('&', '&amp;')
         .gsub('<', '&lt;')
         .gsub('>', '&gt;')
         .gsub('*', '&#42;') # to avoid encountering`*/`
  end

  def convert_constant(str)
    if str.nil? || str.empty?
      str
    elsif str =~ /\A\d+\z/
      str
    else
      "Constants.#{str}"
    end
  end
end
