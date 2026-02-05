require 'xdrgen'
require_relative 'generator/generator'

puts "Generating Java XDR classes..."

# Operate on the root directory of the repo.
Dir.chdir("..")

# Compile the XDR files into Java.
Xdrgen::Compilation.new(
  Dir.glob("xdr/*.x"),
  output_dir: "src/main/java/org/stellar/sdk/xdr/",
  generator: Generator,
  namespace: "org.stellar.sdk.xdr",
).compile

puts "Done!"
