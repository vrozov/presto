#!/usr/bin/env bash

set -xeuo pipefail

suite_exit_code=0
failed_env=""

for conf in singlenode \
    multinode \
    multinode-tls-kerberos \
    singlenode-hdfs-impersonation \
    singlenode-hive-impersonation \
    singlenode-kerberos-hive-impersonation \
    singlenode-kerberos-hdfs-impersonation \
    singlenode-kerberos-hdfs-impersonation-cross-realm \
    singlenode-kerberos-hdfs-impersonation-with-wire-encryption \
    singlenode-kerberos-hdfs-no-impersonation \
    singlenode-kerberos-hive-impersonation \
    singlenode-kerberos-kms-hdfs-impersonation \
    singlenode-kerberos-kms-hdfs-no-impersonation
do
    presto-product-tests/bin/run_on_docker.sh \
        $conf \
        -g iceberg \
        || failed_env="${failed_env}${conf}, "
    echo $failed_env
done

if test -v failed_env; then
    echo $failed_env
    suite_exit_code=1
else
    suite_exit_code=0
fi

echo "$0: exiting with ${suite_exit_code}"
exit "${suite_exit_code}"
