echo "machine github.com login stellar-jenkins password $GITHUB_TOKEN" >~/.netrc
git clone -b gh-pages "https://stellar-jenkins@github.com/stellar/java-stellar-sdk.git" javadoc

if [ ! -d "javadoc" ]; then
  echo "Error cloning"
  exit 1
fi

rm -rf javadoc/* # Remove all files without hidden (.git)
javadoc -public -splitindex -windowtitle "java-stellar-sdk documentation" -d ./javadoc -sourcepath ./src/main/java/ -subpackages org.stellar.sdk -exclude org.stellar.sdk.xdr
cd javadoc
git config user.name "post-release-script"
git config user.email "post-release-script@stellar.org"
git add .
git commit -m $CIRCLE_TAG
git push origin gh-pages
