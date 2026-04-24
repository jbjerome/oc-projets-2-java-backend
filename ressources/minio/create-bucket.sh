#!/bin/sh
set -eu

# Wait for MinIO server to be ready, then create the public rentals bucket.
# Idempotent: safe to re-run on every container start.

mc alias set local "${MINIO_ENDPOINT:-http://minio:9000}" "${MINIO_ROOT_USER}" "${MINIO_ROOT_PASSWORD}"

if ! mc ls "local/${MINIO_BUCKET}" >/dev/null 2>&1; then
  mc mb "local/${MINIO_BUCKET}"
fi

# Public read so the URLs returned by the API are directly fetchable by the browser.
mc anonymous set download "local/${MINIO_BUCKET}"

echo "MinIO bucket '${MINIO_BUCKET}' ready."
