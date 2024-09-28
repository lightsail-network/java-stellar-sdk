# XDR files from https://github.com/stellar/stellar-xdr
XDRS = xdr/Stellar-SCP.x \
xdr/Stellar-ledger-entries.x \
xdr/Stellar-ledger.x \
xdr/Stellar-overlay.x \
xdr/Stellar-transaction.x \
xdr/Stellar-types.x \
xdr/Stellar-contract-env-meta.x \
xdr/Stellar-contract-meta.x \
xdr/Stellar-contract-spec.x \
xdr/Stellar-contract.x \
xdr/Stellar-internal.x \
xdr/Stellar-contract-config-setting.x

# xdrgen commit to use, see https://github.com/stellar/xdrgen
XDRGEN_COMMIT=6d4d47b4bc6815e26a52d62fd6613cfb56676d4b
# stellar-xdr commit to use, see https://github.com/stellar/stellar-xdr
XDR_COMMIT=529d5176f24c73eeccfa5eba481d4e89c19b1181

.PHONY: xdr xdr-clean xdr-update

xdr-generate: $(XDRS)
	docker run -it --rm -v $$PWD:/wd -w /wd ruby /bin/bash -c '\
		gem install specific_install -v 0.3.8 && \
		gem specific_install https://github.com/lightsail-network/xdrgen.git -b $(XDRGEN_COMMIT) && \
		xdrgen \
			--language java \
			--namespace org.stellar.sdk.xdr \
			--output src/main/java/org/stellar/sdk/xdr/ \
			$(XDRS)'
	./gradlew :spotlessApply

xdr/%.x:
	curl -Lsf -o $@ https://raw.githubusercontent.com/stellar/stellar-xdr/$(XDR_COMMIT)/$(@F)

xdr-clean:
	rm xdr/*.x || true
	find src/main/java/org/stellar/sdk/xdr -type f -name "*.java" ! -name "package-info.java" -delete

xdr-update: xdr-clean xdr-generate