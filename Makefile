# XDR files from https://github.com/stellar/stellar-xdr
XDRS = xdr/Stellar-SCP.x \
xdr/Stellar-contract-config-setting.x \
xdr/Stellar-contract-env-meta.x \
xdr/Stellar-contract-meta.x \
xdr/Stellar-contract-spec.x \
xdr/Stellar-contract.x \
xdr/Stellar-exporter.x \
xdr/Stellar-internal.x \
xdr/Stellar-ledger-entries.x \
xdr/Stellar-ledger.x \
xdr/Stellar-overlay.x \
xdr/Stellar-transaction.x \
xdr/Stellar-types.x

# stellar-xdr commit to use, see https://github.com/stellar/stellar-xdr
XDR_COMMIT=0a621ec7811db000a60efae5b35f78dee3aa2533

.PHONY: xdr xdr-clean xdr-update xdr-generator-test xdr-generator-update-snapshots

xdr-generate: $(XDRS)
	docker run --rm -v $$PWD:/wd -w /wd ruby:3.4 /bin/bash -c '\
		cd xdr-generator && \
		bundle install --quiet && \
		bundle exec ruby generate.rb'
	./gradlew :spotlessApply

xdr/%.x:
	curl -Lsf -o $@ https://raw.githubusercontent.com/stellar/stellar-xdr/$(XDR_COMMIT)/$(@F)

xdr-clean:
	rm xdr/*.x || true
	find src/main/java/org/stellar/sdk/xdr -type f -name "*.java" ! -name "package-info.java" -delete

xdr-update: xdr-clean xdr-generate

xdr-generator-test:
	docker run --rm -v $$PWD:/wd -w /wd ruby:3.4 /bin/bash -c '\
		cd xdr-generator && \
		bundle install --quiet && \
		bundle exec ruby test/generator_snapshot_test.rb'

xdr-generator-update-snapshots:
	docker run --rm -v $$PWD:/wd -w /wd ruby:3.4 /bin/bash -c '\
		cd xdr-generator && \
		bundle install --quiet && \
		UPDATE_SNAPSHOTS=1 bundle exec ruby test/generator_snapshot_test.rb'
