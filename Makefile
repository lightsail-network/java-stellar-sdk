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
# TODO: update XDRGEN_COMMIT
XDRGEN_COMMIT=f0c41458ca0b66b4649b18deddc9f7a11199f1f9
XDRNEXT_COMMIT=9ac02641139e6717924fdad716f6e958d0168491

.PHONY: xdr xdr-clean xdr-update

xdr-generate: $(XDRS)
	docker run -it --rm -v $$PWD:/wd -w /wd ruby /bin/bash -c '\
		gem install specific_install -v 0.3.8 && \
		gem specific_install https://github.com/stellar/xdrgen.git -b $(XDRGEN_COMMIT) && \
		xdrgen \
			--language java \
			--namespace org.stellar.sdk.xdr \
			--output src/main/java/org/stellar/sdk/xdr/ \
			$(XDRS)'
	./gradlew :spotlessApply

xdr/%.x:
	curl -Lsf -o $@ https://raw.githubusercontent.com/stellar/stellar-xdr/$(XDRNEXT_COMMIT)/$(@F)

xdr-clean:
	rm xdr/*.x || true
	rm src/main/java/org/stellar/sdk/xdr/*.java || true

xdr-update: xdr-clean xdr-generate