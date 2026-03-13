require 'set'

class Generator < Xdrgen::Generators::Base
  AST = Xdrgen::AST

  STELLAR_SPECIFIC_TYPES = %w[
    AccountID PublicKey NodeID ContractID
    MuxedAccount MuxedEd25519Account SCAddress
    SignerKey SignerKeyEd25519SignedPayload
    PoolID ClaimableBalanceID
    AssetCode4 AssetCode12
    UInt128Parts Int128Parts UInt256Parts Int256Parts
  ].freeze

  STELLAR_STRKEY_TYPES = %w[
    AccountID PublicKey NodeID ContractID
    MuxedAccount MuxedEd25519Account SCAddress
    SignerKey SignerKeyEd25519SignedPayload
    PoolID ClaimableBalanceID
  ].freeze

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
    imports.add("java.util.LinkedHashMap")
    imports.add("java.util.Map")
    imports.add("java.util.List")
    imports.add("java.util.ArrayList")

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

    if needs_strkey_import_for_definition?(defn)
      imports.add("org.stellar.sdk.StrKey")
    end

    if needs_biginteger_import_for_definition?(defn)
      imports.add("java.math.BigInteger")
    end

    if defn.respond_to? :nested_definitions
      defn.nested_definitions.each{ |child_defn| add_imports_for_definition(child_defn, imports) }
    end
  end

  def needs_strkey_import_for_definition?(defn)
    type_name = name(defn) rescue nil
    return false if type_name.nil?
    STELLAR_STRKEY_TYPES.include?(type_name)
  end

  def needs_biginteger_import_for_definition?(defn)
    type_name = name(defn) rescue nil
    return false if type_name.nil?
    %w[UInt128Parts Int128Parts UInt256Parts Int256Parts].include?(type_name)
  end

  def stellar_specific_type?(type_name)
    STELLAR_SPECIFIC_TYPES.include?(type_name)
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

  # ============================================================================
  # Enum rendering
  # ============================================================================

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

    # JSON methods
    render_enum_json(enum, out)

    out.break
  end

  def render_enum_json(enum, out)
    prefix_len = enum_prefix_length(enum)
    enum_name = name_string enum.name

    render_json_public_methods(enum_name, out)

    # toJsonObject
    out.puts "Object toJsonObject() {"
    out.indent do
      out.puts "switch (this) {"
      enum.members.each do |em|
        json_name = enum_json_name(em.name, prefix_len)
        out.puts "case #{em.name}: return \"#{json_name}\";"
      end
      out.puts "default: throw new IllegalArgumentException(\"Unknown enum value: \" + this.value);"
      out.puts "}"
    end
    out.puts "}"

    # fromJsonObject
    out.puts "static #{enum_name} fromJsonObject(Object json) {"
    out.indent do
      out.puts "String value = (String) json;"
      out.puts "switch (value) {"
      enum.members.each do |em|
        json_name = enum_json_name(em.name, prefix_len)
        out.puts "case \"#{json_name}\": return #{em.name};"
      end
      out.puts "default: throw new IllegalArgumentException(\"Unknown JSON value: \" + value);"
      out.puts "}"
    end
    out.puts "}"
  end

  # ============================================================================
  # Struct rendering
  # ============================================================================

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

    # JSON methods
    render_struct_json(struct, out)

    out.break
  end

  def render_struct_json(struct, out)
    struct_name = name struct

    if stellar_specific_type?(struct_name)
      render_stellar_struct_json(struct, struct_name, out)
      return
    end

    render_json_public_methods(struct_name, out)

    # toJsonObject
    out.puts "Object toJsonObject() {"
    out.indent do
      out.puts "LinkedHashMap<String, Object> jsonMap = new LinkedHashMap<>();"
      struct.members.each do |m|
        json_key = m.name.underscore
        expr = encode_value_to_json(m.declaration, m.name, m.type.sub_type == :optional)
        out.puts "jsonMap.put(\"#{json_key}\", #{expr});"
      end
      out.puts "return jsonMap;"
    end
    out.puts "}"

    # fromJsonObject
    out.puts "@SuppressWarnings(\"unchecked\")"
    out.puts "static #{struct_name} fromJsonObject(Object json) {"
    out.indent do
      out.puts "java.util.Map<String, Object> jsonMap = (java.util.Map<String, Object>) json;"
      out.puts "#{struct_name} instance = new #{struct_name}();"
      struct.members.each do |m|
        json_key = m.name.underscore
        decode_expr = decode_value_from_json(m.declaration, "jsonMap.get(\"#{json_key}\")", m.type.sub_type == :optional)
        out.puts "instance.#{m.name} = #{decode_expr};"
      end
      out.puts "return instance;"
    end
    out.puts "}"
  end

  # ============================================================================
  # Typedef rendering
  # ============================================================================

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

    # JSON methods
    render_typedef_json(typedef, out)
  end

  def render_typedef_json(typedef, out)
    typedef_name = name typedef

    if stellar_specific_type?(typedef_name)
      render_stellar_typedef_json(typedef, typedef_name, out)
      return
    end

    render_json_public_methods(typedef_name, out)

    # toJsonObject
    out.puts "Object toJsonObject() {"
    out.indent do
      expr = encode_value_to_json(typedef.declaration, typedef.name, typedef.type.sub_type == :optional)
      out.puts "return #{expr};"
    end
    out.puts "}"

    # fromJsonObject
    out.puts "static #{typedef_name} fromJsonObject(Object json) {"
    out.indent do
      out.puts "#{typedef_name} instance = new #{typedef_name}();"
      decode_expr = decode_value_from_json(typedef.declaration, "json", typedef.type.sub_type == :optional)
      out.puts "instance.#{typedef.name} = #{decode_expr};"
      out.puts "return instance;"
    end
    out.puts "}"
  end

  # ============================================================================
  # Union rendering
  # ============================================================================

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

    # JSON methods
    render_union_json(union, out)

    out.break
  end

  def render_union_json(union, out)
    union_name = name union

    if stellar_specific_type?(union_name)
      render_stellar_union_json(union, union_name, out)
      return
    end

    disc_enum = get_discriminant_enum(union)

    # Collect void and non-void arm JSON keys
    void_keys = []
    non_void_keys = []
    union.normal_arms.each do |arm|
      arm.cases.each do |union_case|
        key = json_key_for_case(union_case, disc_enum)
        if arm.void?
          void_keys << key
        else
          non_void_keys << key
        end
      end
    end
    has_void_default = union.default_arm.present? && union.default_arm.void?

    render_json_public_methods(union_name, out)

    # toJsonObject
    out.puts "Object toJsonObject() {"
    out.indent do
      union.normal_arms.each do |arm|
        arm.cases.each do |union_case|
          json_key = json_key_for_case(union_case, disc_enum)
          condition = render_union_case_condition_java(union, union_case)
          out.puts "if (#{condition}) {"
          out.indent do
            if arm.void?
              out.puts "return \"#{json_key}\";"
            else
              value_expr = encode_union_arm_value_to_json(arm)
              out.puts "LinkedHashMap<String, Object> jsonMap = new LinkedHashMap<>();"
              out.puts "jsonMap.put(\"#{json_key}\", #{value_expr});"
              out.puts "return jsonMap;"
            end
          end
          out.puts "}"
        end
      end

      if union.default_arm.present?
        if union.default_arm.void?
          if disc_enum
            out.puts "return discriminant.toJsonObject();"
          else
            out.puts "return \"v\" + discriminant;"
          end
        else
          value_expr = encode_union_arm_value_to_json(union.default_arm)
          out.puts "LinkedHashMap<String, Object> jsonMap = new LinkedHashMap<>();"
          if disc_enum
            out.puts "jsonMap.put((String) discriminant.toJsonObject(), #{value_expr});"
          else
            out.puts "jsonMap.put(\"v\" + discriminant, #{value_expr});"
          end
          out.puts "return jsonMap;"
        end
      else
        out.puts "throw new IllegalArgumentException(\"Unknown discriminant: \" + discriminant);"
      end
    end
    out.puts "}"

    # fromJsonObject
    out.puts "@SuppressWarnings(\"unchecked\")"
    out.puts "static #{union_name} fromJsonObject(Object json) {"
    out.indent do
      has_void = void_keys.any? || has_void_default
      has_non_void = non_void_keys.any? || (union.default_arm.present? && !union.default_arm.void?)

      if has_void
        out.puts "if (json instanceof String) {"
        out.indent do
          out.puts "String strVal = (String) json;"
          render_union_void_from_json(out, union, union_name, disc_enum, void_keys, non_void_keys, has_void_default)
        end
        out.puts "}"
      end

      unless has_non_void
        out.puts "throw new IllegalArgumentException(\"Expected a string for #{union_name}, got: \" + json);"
      end

      if has_non_void
        out.puts "java.util.Map<String, Object> jsonMap = (java.util.Map<String, Object>) json;"
        out.puts "if (jsonMap.containsKey(\"$schema\")) {"
        out.puts "  jsonMap = new LinkedHashMap<>(jsonMap);"
        out.puts "  jsonMap.remove(\"$schema\");"
        out.puts "}"
        out.puts "if (jsonMap.size() != 1) {"
        out.puts "  throw new IllegalArgumentException(\"Expected a single-key object for #{union_name}, got: \" + json);"
        out.puts "}"
        out.puts "String key = jsonMap.keySet().iterator().next();"

        # Parse discriminant from key
        if disc_enum
          disc_type_name = name disc_enum
          out.puts "#{type_string union.discriminant.type} discriminant = #{disc_type_name}.fromJsonObject(key);"
        else
          disc_type = type_string(union.discriminant.type)
          if disc_type == "Integer"
            out.puts "Integer discriminant = Integer.parseInt(key.substring(1));"
          else
            out.puts "#{disc_type} discriminant = #{disc_type}.fromJsonObject(Integer.parseInt(key.substring(1)));"
          end
        end

        union.normal_arms.each do |arm|
          next if arm.void?
          arm.cases.each do |union_case|
            json_key = json_key_for_case(union_case, disc_enum)
            out.puts "if (key.equals(\"#{json_key}\")) {"
            out.indent do
              decode_expr = decode_union_arm_value_from_json(arm, "jsonMap.get(\"#{json_key}\")")
              out.puts "#{union_name} instance = new #{union_name}();"
              out.puts "instance.discriminant = discriminant;"
              out.puts "instance.#{arm.name} = #{decode_expr};"
              out.puts "return instance;"
            end
            out.puts "}"
          end
        end

        if union.default_arm.present? && !union.default_arm.void?
          decode_expr = decode_union_arm_value_from_json(union.default_arm, "jsonMap.get(key)")
          out.puts "#{union_name} instance = new #{union_name}();"
          out.puts "instance.discriminant = discriminant;"
          out.puts "instance.#{union.default_arm.name} = #{decode_expr};"
          out.puts "return instance;"
        else
          out.puts "throw new IllegalArgumentException(\"Unknown key '\" + key + \"' for #{union_name}\");"
        end
      end
    end
    out.puts "}"
  end

  def render_union_void_from_json(out, union, union_name, disc_enum, void_keys, non_void_keys, has_void_default)
    if has_void_default
      # Void default arm: string input valid for void arms and default
      if non_void_keys.any?
        nv_checks = non_void_keys.map { |k| "strVal.equals(\"#{k}\")" }.join(" || ")
        out.puts "if (#{nv_checks}) {"
        out.puts "  throw new IllegalArgumentException(\"'\" + strVal + \"' requires a value for #{union_name}, use dict form instead\");"
        out.puts "}"
      end
      if disc_enum
        disc_type_name = name disc_enum
        out.puts "#{union_name} instance = new #{union_name}();"
        out.puts "instance.discriminant = #{disc_type_name}.fromJsonObject(strVal);"
        out.puts "return instance;"
      else
        out.puts "#{union_name} instance = new #{union_name}();"
        out.puts "instance.discriminant = Integer.parseInt(strVal.substring(1));"
        out.puts "return instance;"
      end
    elsif void_keys.any?
      # Only specific void arms
      void_checks = void_keys.map { |k| "strVal.equals(\"#{k}\")" }.join(" || ")
      out.puts "if (!(#{void_checks})) {"
      out.puts "  throw new IllegalArgumentException(\"Unexpected string '\" + strVal + \"' for #{union_name}\");"
      out.puts "}"
      if disc_enum
        disc_type_name = name disc_enum
        out.puts "#{union_name} instance = new #{union_name}();"
        out.puts "instance.discriminant = #{disc_type_name}.fromJsonObject(strVal);"
        out.puts "return instance;"
      else
        out.puts "#{union_name} instance = new #{union_name}();"
        out.puts "instance.discriminant = Integer.parseInt(strVal.substring(1));"
        out.puts "return instance;"
      end
    end
  end

  # ============================================================================
  # Top matter & helpers
  # ============================================================================

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

  def render_json_public_methods(type_name, out)
    out.puts <<-EOS.strip_heredoc
      @Override
      public String toJson() {
        return XdrElement.gson.toJson(toJsonObject());
      }

      public static #{type_name} fromJson(String json) {
        return fromJsonObject(XdrElement.gson.fromJson(json, Object.class));
      }
    EOS
  end

  # ============================================================================
  # Encode/decode members (XDR binary)
  # ============================================================================

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

  # ============================================================================
  # JSON encoding/decoding helpers (SEP-0051)
  # ============================================================================

  # Returns a Java expression that converts a field value to its JSON representation
  def encode_type_to_json(decl, value)
    case decl.type
    when AST::Typespecs::Int
      "(Integer) #{value}"
    when AST::Typespecs::UnsignedInt
      "#{value}.toJsonObject()"
    when AST::Typespecs::Hyper
      "#{value}.toString()"
    when AST::Typespecs::UnsignedHyper
      "#{value}.toJsonObject()"
    when AST::Typespecs::Float
      "(Float) #{value}"
    when AST::Typespecs::Double
      "(Double) #{value}"
    when AST::Typespecs::Bool
      "(Boolean) #{value}"
    when AST::Typespecs::Opaque
      "XdrElement.bytesToHex(#{value})"
    when AST::Typespecs::String
      "#{value}.toJsonObject()"
    when AST::Typespecs::Simple, AST::Concerns::NestedDefinition
      "#{value}.toJsonObject()"
    else
      raise "Unknown typespec for JSON encode: #{decl.type.class.name}"
    end
  end

  # Returns a Java expression that converts a JSON value back to the XDR field type
  def decode_type_from_json(decl, json_expr)
    case decl.type
    when AST::Typespecs::Int
      "((Number) #{json_expr}).intValue()"
    when AST::Typespecs::UnsignedInt
      "XdrUnsignedInteger.fromJsonObject(#{json_expr})"
    when AST::Typespecs::Hyper
      "XdrElement.jsonToLong(#{json_expr})"
    when AST::Typespecs::UnsignedHyper
      "XdrUnsignedHyperInteger.fromJsonObject(#{json_expr})"
    when AST::Typespecs::Float
      "((Number) #{json_expr}).floatValue()"
    when AST::Typespecs::Double
      "((Number) #{json_expr}).doubleValue()"
    when AST::Typespecs::Bool
      "(Boolean) #{json_expr}"
    when AST::Typespecs::Opaque
      "XdrElement.hexToBytes((String) #{json_expr})"
    when AST::Typespecs::String
      "XdrString.fromJsonObject(#{json_expr})"
    when AST::Typespecs::Simple
      "#{name decl.type.resolved_type}.fromJsonObject(#{json_expr})"
    when AST::Concerns::NestedDefinition
      "#{name decl.type}.fromJsonObject(#{json_expr})"
    else
      raise "Unknown typespec for JSON decode: #{decl.type.class.name}"
    end
  end

  # Wraps encode_type_to_json for arrays/optionals
  def encode_value_to_json(decl, value, is_optional = false)
    case decl
    when AST::Declarations::Array
      item_expr = encode_type_to_json(decl, "#{value}[i]")
      "XdrElement.arrayToJsonArray(#{value}, i -> #{item_expr})"
    when AST::Declarations::Opaque
      "XdrElement.bytesToHex(#{value})"
    when AST::Declarations::String
      "#{value}.toJsonObject()"
    else
      if is_optional
        "#{value} != null ? #{encode_type_to_json(decl, value)} : null"
      else
        encode_type_to_json(decl, value)
      end
    end
  end

  # Wraps decode_type_from_json for arrays/optionals
  def decode_value_from_json(decl, json_expr, is_optional = false)
    case decl
    when AST::Declarations::Array
      item_type = type_string(decl.type)
      item_decode = decode_type_from_json(decl, "item")
      "XdrElement.jsonArrayToArray((List<Object>) #{json_expr}, #{item_type}.class, item -> #{item_decode})"
    when AST::Declarations::Opaque
      "XdrElement.hexToBytes((String) #{json_expr})"
    when AST::Declarations::String
      "XdrString.fromJsonObject(#{json_expr})"
    else
      if is_optional
        "#{json_expr} != null ? #{decode_type_from_json(decl, json_expr)} : null"
      else
        decode_type_from_json(decl, json_expr)
      end
    end
  end

  # Union arm JSON encode helper
  def encode_union_arm_value_to_json(arm)
    case arm.declaration
    when AST::Declarations::Array
      item_expr = encode_type_to_json(arm.declaration, "#{arm.name}[i]")
      "XdrElement.arrayToJsonArray(#{arm.name}, i -> #{item_expr})"
    when AST::Declarations::Opaque
      "XdrElement.bytesToHex(#{arm.name})"
    when AST::Declarations::String
      "#{arm.name}.toJsonObject()"
    else
      encode_type_to_json(arm.declaration, arm.name)
    end
  end

  # Union arm JSON decode helper
  def decode_union_arm_value_from_json(arm, json_expr)
    case arm.declaration
    when AST::Declarations::Array
      item_type = type_string(arm.declaration.type)
      item_decode = decode_type_from_json(arm.declaration, "item")
      "XdrElement.jsonArrayToArray((List<Object>) #{json_expr}, #{item_type}.class, item -> #{item_decode})"
    when AST::Declarations::Opaque
      "XdrElement.hexToBytes((String) #{json_expr})"
    when AST::Declarations::String
      "XdrString.fromJsonObject(#{json_expr})"
    else
      decode_type_from_json(arm.declaration, json_expr)
    end
  end

  # ============================================================================
  # Enum JSON helpers
  # ============================================================================

  def enum_prefix_length(enum)
    names = enum.members.map { |m| m.name }
    return 0 if names.size <= 1

    prefix = names.first.dup
    names[1..].each do |n|
      while !n.start_with?(prefix) && !prefix.empty?
        prefix = prefix[0...-1]
      end
    end

    return 0 if prefix.empty?

    last_underscore = prefix.rindex('_')
    return 0 if last_underscore.nil?

    last_underscore + 1
  end

  def enum_json_name(member_name, prefix_len)
    member_name[prefix_len..].downcase
  end

  # ============================================================================
  # Union JSON helpers
  # ============================================================================

  def get_discriminant_enum(union)
    type = union.discriminant.type
    case type
    when AST::Typespecs::Simple
      resolved = type.resolved_type
      return resolved if resolved.is_a?(AST::Definitions::Enum)
    when AST::Concerns::NestedDefinition
      return type if type.is_a?(AST::Definitions::Enum)
    end
    nil
  end

  def json_key_for_case(union_case, disc_enum)
    if union_case.value.is_a?(AST::Identifier)
      case_name = union_case.value.name
      if disc_enum
        prefix_len = enum_prefix_length(disc_enum)
        enum_json_name(case_name, prefix_len)
      else
        case_name.downcase
      end
    else
      "v#{union_case.value.value}"
    end
  end

  def render_union_case_condition_java(union, union_case)
    if union_case.value.is_a?(AST::Identifier)
      if union.discriminant.type.is_a?(AST::Typespecs::Int)
        member = union.resolved_case(union_case)
        "discriminant == #{member.value}"
      elsif type_string(union.discriminant.type) == "Uint32"
        "discriminant.getUint32().getNumber().intValue() == #{union.resolved_case(union_case).value}"
      elsif type_string(union.discriminant.type) == "Integer"
        member = union.resolved_case(union_case)
        "discriminant == #{member.value}"
      else
        "discriminant == #{type_string(union.discriminant.type)}.#{union_case.value.name}"
      end
    else
      if union.discriminant.type.is_a?(AST::Typespecs::Int)
        "discriminant == #{union_case.value.value}"
      elsif type_string(union.discriminant.type) == "Uint32"
        "discriminant.getUint32().getNumber().intValue() == #{union_case.value.value}"
      else
        "discriminant == #{union_case.value.value}"
      end
    end
  end

  # ============================================================================
  # Stellar-specific JSON: Structs
  # ============================================================================

  def render_stellar_struct_json(struct, struct_name, out)
    render_json_public_methods(struct_name, out)

    case struct_name
    when "MuxedEd25519Account"
      out.puts <<-EOS.strip_heredoc
        Object toJsonObject() {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                XdrDataOutputStream xdrOut = new XdrDataOutputStream(baos);
                this.ed25519.encode(xdrOut);
                this.id.encode(xdrOut);
                return StrKey.encodeMed25519PublicKey(baos.toByteArray());
            } catch (IOException e) {
                throw new IllegalArgumentException("Failed to encode MuxedEd25519Account", e);
            }
        }
        static MuxedEd25519Account fromJsonObject(Object json) {
            String strKey = (String) json;
            try {
                byte[] raw = StrKey.decodeMed25519PublicKey(strKey);
                ByteArrayInputStream bais = new ByteArrayInputStream(raw);
                XdrDataInputStream xdrIn = new XdrDataInputStream(bais);
                Uint256 ed25519 = Uint256.decode(xdrIn);
                Uint64 id = Uint64.decode(xdrIn);
                MuxedEd25519Account instance = new MuxedEd25519Account();
                instance.id = id;
                instance.ed25519 = ed25519;
                return instance;
            } catch (IOException e) {
                throw new IllegalArgumentException("Failed to decode MuxedEd25519Account", e);
            }
        }
      EOS
    when "SignerKeyEd25519SignedPayload"
      out.puts <<-EOS.strip_heredoc
        Object toJsonObject() {
            try {
                return StrKey.encodeSignedPayload(this.toXdrByteArray());
            } catch (IOException e) {
                throw new IllegalArgumentException("Failed to encode SignerKeyEd25519SignedPayload", e);
            }
        }
        static SignerKeyEd25519SignedPayload fromJsonObject(Object json) {
            String strKey = (String) json;
            try {
                return SignerKeyEd25519SignedPayload.fromXdrByteArray(StrKey.decodeSignedPayload(strKey));
            } catch (IOException e) {
                throw new IllegalArgumentException("Failed to decode SignerKeyEd25519SignedPayload", e);
            }
        }
      EOS
    when "UInt128Parts"
      out.puts <<-EOS.strip_heredoc
        Object toJsonObject() {
            byte[] bytes = new byte[16];
            byte[] hiBytes = hi.getUint64().getNumber().toByteArray();
            byte[] loBytes = lo.getUint64().getNumber().toByteArray();
            System.arraycopy(hiBytes, Math.max(0, hiBytes.length - 8), bytes, 8 - Math.min(8, hiBytes.length), Math.min(8, hiBytes.length));
            System.arraycopy(loBytes, Math.max(0, loBytes.length - 8), bytes, 16 - Math.min(8, loBytes.length), Math.min(8, loBytes.length));
            return new BigInteger(1, bytes).toString();
        }
        static UInt128Parts fromJsonObject(Object json) {
            BigInteger value = new BigInteger((String) json);
            byte[] bytes = new byte[16];
            byte[] valBytes = value.toByteArray();
            int copyLen = Math.min(valBytes.length, 16);
            System.arraycopy(valBytes, Math.max(0, valBytes.length - 16), bytes, 16 - copyLen, copyLen);
            BigInteger hiVal = new BigInteger(1, java.util.Arrays.copyOfRange(bytes, 0, 8));
            BigInteger loVal = new BigInteger(1, java.util.Arrays.copyOfRange(bytes, 8, 16));
            UInt128Parts instance = new UInt128Parts();
            instance.hi = new Uint64(new XdrUnsignedHyperInteger(hiVal));
            instance.lo = new Uint64(new XdrUnsignedHyperInteger(loVal));
            return instance;
        }
      EOS
    when "Int128Parts"
      out.puts <<-EOS.strip_heredoc
        Object toJsonObject() {
            byte[] bytes = new byte[16];
            long hiVal = hi.getInt64();
            BigInteger loVal = lo.getUint64().getNumber();
            byte[] hiBytes = BigInteger.valueOf(hiVal).toByteArray();
            byte[] loBytes = loVal.toByteArray();
            // hi is signed 64-bit
            if (hiVal < 0) {
                java.util.Arrays.fill(bytes, 0, 8, (byte) 0xFF);
            }
            System.arraycopy(hiBytes, Math.max(0, hiBytes.length - 8), bytes, 8 - Math.min(8, hiBytes.length), Math.min(8, hiBytes.length));
            System.arraycopy(loBytes, Math.max(0, loBytes.length - 8), bytes, 16 - Math.min(8, loBytes.length), Math.min(8, loBytes.length));
            return new BigInteger(bytes).toString();
        }
        static Int128Parts fromJsonObject(Object json) {
            BigInteger value = new BigInteger((String) json);
            byte[] bytes = new byte[16];
            byte[] valBytes = value.toByteArray();
            if (value.signum() < 0) {
                java.util.Arrays.fill(bytes, (byte) 0xFF);
            }
            int copyLen = Math.min(valBytes.length, 16);
            System.arraycopy(valBytes, Math.max(0, valBytes.length - 16), bytes, 16 - copyLen, copyLen);
            long hiVal = new BigInteger(java.util.Arrays.copyOfRange(bytes, 0, 8)).longValue();
            BigInteger loVal = new BigInteger(1, java.util.Arrays.copyOfRange(bytes, 8, 16));
            Int128Parts instance = new Int128Parts();
            instance.hi = new Int64(hiVal);
            instance.lo = new Uint64(new XdrUnsignedHyperInteger(loVal));
            return instance;
        }
      EOS
    when "UInt256Parts"
      out.puts <<-EOS.strip_heredoc
        Object toJsonObject() {
            byte[] bytes = new byte[32];
            byte[] hhBytes = hi_hi.getUint64().getNumber().toByteArray();
            byte[] hlBytes = hi_lo.getUint64().getNumber().toByteArray();
            byte[] lhBytes = lo_hi.getUint64().getNumber().toByteArray();
            byte[] llBytes = lo_lo.getUint64().getNumber().toByteArray();
            System.arraycopy(hhBytes, Math.max(0, hhBytes.length - 8), bytes, 8 - Math.min(8, hhBytes.length), Math.min(8, hhBytes.length));
            System.arraycopy(hlBytes, Math.max(0, hlBytes.length - 8), bytes, 16 - Math.min(8, hlBytes.length), Math.min(8, hlBytes.length));
            System.arraycopy(lhBytes, Math.max(0, lhBytes.length - 8), bytes, 24 - Math.min(8, lhBytes.length), Math.min(8, lhBytes.length));
            System.arraycopy(llBytes, Math.max(0, llBytes.length - 8), bytes, 32 - Math.min(8, llBytes.length), Math.min(8, llBytes.length));
            return new BigInteger(1, bytes).toString();
        }
        static UInt256Parts fromJsonObject(Object json) {
            BigInteger value = new BigInteger((String) json);
            byte[] bytes = new byte[32];
            byte[] valBytes = value.toByteArray();
            int copyLen = Math.min(valBytes.length, 32);
            System.arraycopy(valBytes, Math.max(0, valBytes.length - 32), bytes, 32 - copyLen, copyLen);
            BigInteger hhVal = new BigInteger(1, java.util.Arrays.copyOfRange(bytes, 0, 8));
            BigInteger hlVal = new BigInteger(1, java.util.Arrays.copyOfRange(bytes, 8, 16));
            BigInteger lhVal = new BigInteger(1, java.util.Arrays.copyOfRange(bytes, 16, 24));
            BigInteger llVal = new BigInteger(1, java.util.Arrays.copyOfRange(bytes, 24, 32));
            UInt256Parts instance = new UInt256Parts();
            instance.hi_hi = new Uint64(new XdrUnsignedHyperInteger(hhVal));
            instance.hi_lo = new Uint64(new XdrUnsignedHyperInteger(hlVal));
            instance.lo_hi = new Uint64(new XdrUnsignedHyperInteger(lhVal));
            instance.lo_lo = new Uint64(new XdrUnsignedHyperInteger(llVal));
            return instance;
        }
      EOS
    when "Int256Parts"
      out.puts <<-EOS.strip_heredoc
        Object toJsonObject() {
            byte[] bytes = new byte[32];
            long hhVal = hi_hi.getInt64();
            BigInteger hlVal = hi_lo.getUint64().getNumber();
            BigInteger lhVal = lo_hi.getUint64().getNumber();
            BigInteger llVal = lo_lo.getUint64().getNumber();
            byte[] hhBytes = BigInteger.valueOf(hhVal).toByteArray();
            byte[] hlBytes = hlVal.toByteArray();
            byte[] lhBytes = lhVal.toByteArray();
            byte[] llBytes = llVal.toByteArray();
            if (hhVal < 0) {
                java.util.Arrays.fill(bytes, 0, 8, (byte) 0xFF);
            }
            System.arraycopy(hhBytes, Math.max(0, hhBytes.length - 8), bytes, 8 - Math.min(8, hhBytes.length), Math.min(8, hhBytes.length));
            System.arraycopy(hlBytes, Math.max(0, hlBytes.length - 8), bytes, 16 - Math.min(8, hlBytes.length), Math.min(8, hlBytes.length));
            System.arraycopy(lhBytes, Math.max(0, lhBytes.length - 8), bytes, 24 - Math.min(8, lhBytes.length), Math.min(8, lhBytes.length));
            System.arraycopy(llBytes, Math.max(0, llBytes.length - 8), bytes, 32 - Math.min(8, llBytes.length), Math.min(8, llBytes.length));
            return new BigInteger(bytes).toString();
        }
        static Int256Parts fromJsonObject(Object json) {
            BigInteger value = new BigInteger((String) json);
            byte[] bytes = new byte[32];
            byte[] valBytes = value.toByteArray();
            if (value.signum() < 0) {
                java.util.Arrays.fill(bytes, (byte) 0xFF);
            }
            int copyLen = Math.min(valBytes.length, 32);
            System.arraycopy(valBytes, Math.max(0, valBytes.length - 32), bytes, 32 - copyLen, copyLen);
            long hhVal = new BigInteger(java.util.Arrays.copyOfRange(bytes, 0, 8)).longValue();
            BigInteger hlVal = new BigInteger(1, java.util.Arrays.copyOfRange(bytes, 8, 16));
            BigInteger lhVal = new BigInteger(1, java.util.Arrays.copyOfRange(bytes, 16, 24));
            BigInteger llVal = new BigInteger(1, java.util.Arrays.copyOfRange(bytes, 24, 32));
            Int256Parts instance = new Int256Parts();
            instance.hi_hi = new Int64(hhVal);
            instance.hi_lo = new Uint64(new XdrUnsignedHyperInteger(hlVal));
            instance.lo_hi = new Uint64(new XdrUnsignedHyperInteger(lhVal));
            instance.lo_lo = new Uint64(new XdrUnsignedHyperInteger(llVal));
            return instance;
        }
      EOS
    end
  end

  # ============================================================================
  # Stellar-specific JSON: Unions
  # ============================================================================

  def render_stellar_union_json(union, union_name, out)
    render_json_public_methods(union_name, out)

    case union_name
    when "PublicKey"
      out.puts <<-EOS.strip_heredoc
        Object toJsonObject() {
            return StrKey.encodeEd25519PublicKey(this.ed25519.getUint256());
        }
        static PublicKey fromJsonObject(Object json) {
            String strKey = (String) json;
            byte[] raw = StrKey.decodeEd25519PublicKey(strKey);
            PublicKey instance = new PublicKey();
            instance.discriminant = PublicKeyType.PUBLIC_KEY_TYPE_ED25519;
            instance.ed25519 = new Uint256();
            instance.ed25519.setUint256(raw);
            return instance;
        }
      EOS
    when "MuxedAccount"
      out.puts <<-EOS.strip_heredoc
        Object toJsonObject() {
            if (this.discriminant == CryptoKeyType.KEY_TYPE_ED25519) {
                return StrKey.encodeEd25519PublicKey(this.ed25519.getUint256());
            }
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                XdrDataOutputStream xdrOut = new XdrDataOutputStream(baos);
                this.med25519.getEd25519().encode(xdrOut);
                this.med25519.getId().encode(xdrOut);
                return StrKey.encodeMed25519PublicKey(baos.toByteArray());
            } catch (IOException e) {
                throw new IllegalArgumentException("Failed to encode MuxedAccount", e);
            }
        }
        static MuxedAccount fromJsonObject(Object json) {
            String strKey = (String) json;
            MuxedAccount instance = new MuxedAccount();
            if (strKey.startsWith("G")) {
                byte[] raw = StrKey.decodeEd25519PublicKey(strKey);
                instance.discriminant = CryptoKeyType.KEY_TYPE_ED25519;
                instance.ed25519 = new Uint256();
                instance.ed25519.setUint256(raw);
            } else {
                try {
                    byte[] raw = StrKey.decodeMed25519PublicKey(strKey);
                    ByteArrayInputStream bais = new ByteArrayInputStream(raw);
                    XdrDataInputStream xdrIn = new XdrDataInputStream(bais);
                    Uint256 ed25519 = Uint256.decode(xdrIn);
                    Uint64 id = Uint64.decode(xdrIn);
                    MuxedAccountMed25519 med = new MuxedAccountMed25519();
                    med.setId(id);
                    med.setEd25519(ed25519);
                    instance.discriminant = CryptoKeyType.KEY_TYPE_MUXED_ED25519;
                    instance.med25519 = med;
                } catch (IOException e) {
                    throw new IllegalArgumentException("Failed to decode MuxedAccount", e);
                }
            }
            return instance;
        }
      EOS
    when "SCAddress"
      out.puts <<-EOS.strip_heredoc
        Object toJsonObject() {
            if (this.discriminant == SCAddressType.SC_ADDRESS_TYPE_ACCOUNT) {
                return this.accountId.toJsonObject();
            }
            if (this.discriminant == SCAddressType.SC_ADDRESS_TYPE_CONTRACT) {
                return StrKey.encodeContract(this.contractId.getContractID().getHash());
            }
            if (this.discriminant == SCAddressType.SC_ADDRESS_TYPE_MUXED_ACCOUNT) {
                try {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    XdrDataOutputStream xdrOut = new XdrDataOutputStream(baos);
                    this.muxedAccount.getEd25519().encode(xdrOut);
                    this.muxedAccount.getId().encode(xdrOut);
                    return StrKey.encodeMed25519PublicKey(baos.toByteArray());
                } catch (IOException e) {
                    throw new IllegalArgumentException("Failed to encode SCAddress", e);
                }
            }
            if (this.discriminant == SCAddressType.SC_ADDRESS_TYPE_CLAIMABLE_BALANCE) {
                return this.claimableBalanceId.toJsonObject();
            }
            if (this.discriminant == SCAddressType.SC_ADDRESS_TYPE_LIQUIDITY_POOL) {
                return this.liquidityPoolId.toJsonObject();
            }
            throw new IllegalArgumentException("Unknown SCAddress type: " + this.discriminant);
        }
        static SCAddress fromJsonObject(Object json) {
            String strKey = (String) json;
            SCAddress instance = new SCAddress();
            if (strKey.startsWith("G")) {
                instance.discriminant = SCAddressType.SC_ADDRESS_TYPE_ACCOUNT;
                instance.accountId = AccountID.fromJsonObject(strKey);
                return instance;
            }
            if (strKey.startsWith("C")) {
                byte[] raw = StrKey.decodeContract(strKey);
                instance.discriminant = SCAddressType.SC_ADDRESS_TYPE_CONTRACT;
                ContractID contractId = new ContractID();
                Hash hash = new Hash();
                hash.setHash(raw);
                contractId.setContractID(hash);
                instance.contractId = contractId;
                return instance;
            }
            if (strKey.startsWith("M")) {
                try {
                    byte[] raw = StrKey.decodeMed25519PublicKey(strKey);
                    ByteArrayInputStream bais = new ByteArrayInputStream(raw);
                    XdrDataInputStream xdrIn = new XdrDataInputStream(bais);
                    Uint256 ed25519 = Uint256.decode(xdrIn);
                    Uint64 id = Uint64.decode(xdrIn);
                    MuxedEd25519Account med = new MuxedEd25519Account();
                    med.setId(id);
                    med.setEd25519(ed25519);
                    instance.discriminant = SCAddressType.SC_ADDRESS_TYPE_MUXED_ACCOUNT;
                    instance.muxedAccount = med;
                } catch (IOException e) {
                    throw new IllegalArgumentException("Failed to decode SCAddress", e);
                }
                return instance;
            }
            if (strKey.startsWith("B")) {
                instance.discriminant = SCAddressType.SC_ADDRESS_TYPE_CLAIMABLE_BALANCE;
                instance.claimableBalanceId = ClaimableBalanceID.fromJsonObject(strKey);
                return instance;
            }
            if (strKey.startsWith("L")) {
                instance.discriminant = SCAddressType.SC_ADDRESS_TYPE_LIQUIDITY_POOL;
                instance.liquidityPoolId = PoolID.fromJsonObject(strKey);
                return instance;
            }
            throw new IllegalArgumentException("Invalid SCAddress strkey: " + strKey);
        }
      EOS
    when "SignerKey"
      out.puts <<-EOS.strip_heredoc
        Object toJsonObject() {
            if (this.discriminant == SignerKeyType.SIGNER_KEY_TYPE_ED25519) {
                return StrKey.encodeEd25519PublicKey(this.ed25519.getUint256());
            }
            if (this.discriminant == SignerKeyType.SIGNER_KEY_TYPE_PRE_AUTH_TX) {
                return StrKey.encodePreAuthTx(this.preAuthTx.getUint256());
            }
            if (this.discriminant == SignerKeyType.SIGNER_KEY_TYPE_HASH_X) {
                return StrKey.encodeSha256Hash(this.hashX.getUint256());
            }
            if (this.discriminant == SignerKeyType.SIGNER_KEY_TYPE_ED25519_SIGNED_PAYLOAD) {
                try {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    XdrDataOutputStream xdrOut = new XdrDataOutputStream(baos);
                    this.ed25519SignedPayload.encode(xdrOut);
                    return StrKey.encodeSignedPayload(baos.toByteArray());
                } catch (IOException e) {
                    throw new IllegalArgumentException("Failed to encode SignerKey", e);
                }
            }
            throw new IllegalArgumentException("Unknown SignerKey type: " + this.discriminant);
        }
        static SignerKey fromJsonObject(Object json) {
            String strKey = (String) json;
            SignerKey instance = new SignerKey();
            if (strKey.startsWith("G")) {
                byte[] raw = StrKey.decodeEd25519PublicKey(strKey);
                instance.discriminant = SignerKeyType.SIGNER_KEY_TYPE_ED25519;
                instance.ed25519 = new Uint256();
                instance.ed25519.setUint256(raw);
                return instance;
            }
            if (strKey.startsWith("T")) {
                byte[] raw = StrKey.decodePreAuthTx(strKey);
                instance.discriminant = SignerKeyType.SIGNER_KEY_TYPE_PRE_AUTH_TX;
                instance.preAuthTx = new Uint256();
                instance.preAuthTx.setUint256(raw);
                return instance;
            }
            if (strKey.startsWith("X")) {
                byte[] raw = StrKey.decodeSha256Hash(strKey);
                instance.discriminant = SignerKeyType.SIGNER_KEY_TYPE_HASH_X;
                instance.hashX = new Uint256();
                instance.hashX.setUint256(raw);
                return instance;
            }
            if (strKey.startsWith("P")) {
                try {
                    byte[] raw = StrKey.decodeSignedPayload(strKey);
                    ByteArrayInputStream bais = new ByteArrayInputStream(raw);
                    XdrDataInputStream xdrIn = new XdrDataInputStream(bais);
                    SignerKeyEd25519SignedPayload payload = SignerKeyEd25519SignedPayload.decode(xdrIn);
                    instance.discriminant = SignerKeyType.SIGNER_KEY_TYPE_ED25519_SIGNED_PAYLOAD;
                    instance.ed25519SignedPayload = payload;
                } catch (IOException e) {
                    throw new IllegalArgumentException("Failed to decode SignerKey", e);
                }
                return instance;
            }
            throw new IllegalArgumentException("Invalid SignerKey strkey: " + strKey);
        }
      EOS
    when "ClaimableBalanceID"
      out.puts <<-EOS.strip_heredoc
        Object toJsonObject() {
            try {
                byte[] xdrBytes = this.toXdrByteArray();
                // The B... strkey payload is the 32-byte balance body without the leading
                // 3 zero bytes from the 4-byte XDR discriminant for v0 (00 00 00 00).
                byte[] raw = new byte[xdrBytes.length - 3];
                System.arraycopy(xdrBytes, 3, raw, 0, raw.length);
                return StrKey.encodeClaimableBalance(raw);
            } catch (IOException e) {
                throw new IllegalArgumentException("Failed to encode ClaimableBalanceID", e);
            }
        }
        static ClaimableBalanceID fromJsonObject(Object json) {
            String strKey = (String) json;
            try {
                byte[] raw = StrKey.decodeClaimableBalance(strKey);
                // Rebuild the XDR form by restoring the omitted 3 leading zero bytes from the
                // v0 discriminant before decoding the full union value.
                byte[] xdrBytes = new byte[raw.length + 3];
                xdrBytes[2] = 0;
                System.arraycopy(raw, 0, xdrBytes, 3, raw.length);
                return ClaimableBalanceID.fromXdrByteArray(xdrBytes);
            } catch (IOException e) {
                throw new IllegalArgumentException("Failed to decode ClaimableBalanceID", e);
            }
        }
      EOS
    end
  end

  # ============================================================================
  # Stellar-specific JSON: Typedefs
  # ============================================================================

  def render_stellar_typedef_json(typedef, typedef_name, out)
    render_json_public_methods(typedef_name, out)

    case typedef_name
    when "AccountID"
      out.puts <<-EOS.strip_heredoc
        Object toJsonObject() {
            return StrKey.encodeEd25519PublicKey(this.AccountID.getEd25519().getUint256());
        }
        static AccountID fromJsonObject(Object json) {
            String strKey = (String) json;
            byte[] raw = StrKey.decodeEd25519PublicKey(strKey);
            PublicKey pk = new PublicKey();
            pk.setDiscriminant(PublicKeyType.PUBLIC_KEY_TYPE_ED25519);
            Uint256 ed25519 = new Uint256();
            ed25519.setUint256(raw);
            pk.setEd25519(ed25519);
            AccountID instance = new AccountID();
            instance.AccountID = pk;
            return instance;
        }
      EOS
    when "NodeID"
      out.puts <<-EOS.strip_heredoc
        Object toJsonObject() {
            return StrKey.encodeEd25519PublicKey(this.NodeID.getEd25519().getUint256());
        }
        static NodeID fromJsonObject(Object json) {
            String strKey = (String) json;
            byte[] raw = StrKey.decodeEd25519PublicKey(strKey);
            PublicKey pk = new PublicKey();
            pk.setDiscriminant(PublicKeyType.PUBLIC_KEY_TYPE_ED25519);
            Uint256 ed25519 = new Uint256();
            ed25519.setUint256(raw);
            pk.setEd25519(ed25519);
            NodeID instance = new NodeID();
            instance.NodeID = pk;
            return instance;
        }
      EOS
    when "ContractID"
      out.puts <<-EOS.strip_heredoc
        Object toJsonObject() {
            return StrKey.encodeContract(this.ContractID.getHash());
        }
        static ContractID fromJsonObject(Object json) {
            String strKey = (String) json;
            byte[] raw = StrKey.decodeContract(strKey);
            Hash hash = new Hash();
            hash.setHash(raw);
            ContractID instance = new ContractID();
            instance.ContractID = hash;
            return instance;
        }
      EOS
    when "PoolID"
      out.puts <<-EOS.strip_heredoc
        Object toJsonObject() {
            return StrKey.encodeLiquidityPool(this.PoolID.getHash());
        }
        static PoolID fromJsonObject(Object json) {
            String strKey = (String) json;
            byte[] raw = StrKey.decodeLiquidityPool(strKey);
            Hash hash = new Hash();
            hash.setHash(raw);
            PoolID instance = new PoolID();
            instance.PoolID = hash;
            return instance;
        }
      EOS
    when "AssetCode4"
      out.puts <<-EOS.strip_heredoc
        Object toJsonObject() {
            int end = this.AssetCode4.length;
            while (end > 0 && this.AssetCode4[end - 1] == 0) {
                end--;
            }
            return XdrElement.bytesToEscapedAscii(java.util.Arrays.copyOf(this.AssetCode4, end));
        }
        static AssetCode4 fromJsonObject(Object json) {
            String value = (String) json;
            byte[] ascii = XdrElement.escapedAsciiToBytes(value);
            if (ascii.length > 4) {
                throw new IllegalArgumentException("AssetCode4 JSON value exceeds 4 bytes");
            }
            byte[] padded = new byte[4];
            System.arraycopy(ascii, 0, padded, 0, Math.min(ascii.length, 4));
            AssetCode4 instance = new AssetCode4();
            instance.AssetCode4 = padded;
            return instance;
        }
      EOS
    when "AssetCode12"
      out.puts <<-EOS.strip_heredoc
        Object toJsonObject() {
            int end = this.AssetCode12.length;
            while (end > 0 && this.AssetCode12[end - 1] == 0) {
                end--;
            }
            int len = Math.max(end, 5);
            return XdrElement.bytesToEscapedAscii(java.util.Arrays.copyOf(this.AssetCode12, len));
        }
        static AssetCode12 fromJsonObject(Object json) {
            String value = (String) json;
            byte[] ascii = XdrElement.escapedAsciiToBytes(value);
            if (ascii.length < 5) {
                throw new IllegalArgumentException("AssetCode12 JSON value must encode at least 5 bytes");
            }
            if (ascii.length > 12) {
                throw new IllegalArgumentException("AssetCode12 JSON value exceeds 12 bytes");
            }
            byte[] padded = new byte[12];
            System.arraycopy(ascii, 0, padded, 0, Math.min(ascii.length, 12));
            AssetCode12 instance = new AssetCode12();
            instance.AssetCode12 = padded;
            return instance;
        }
      EOS
    end
  end

  # ============================================================================
  # Type helpers
  # ============================================================================

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
