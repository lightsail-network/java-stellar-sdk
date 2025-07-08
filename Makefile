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
xdr/Stellar-contract-config-setting.x \
xdr/Stellar-exporter.x

# xdrgen commit to use, see https://github.com/stellar/xdrgen
XDRGEN_COMMIT=6b98787dcbb2b6407880adec5f74c6d88975ab0f
# stellar-xdr commit to use, see https://github.com/stellar/stellar-xdr
XDR_COMMIT=4b7a2ef7931ab2ca2499be68d849f38190b443ca

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