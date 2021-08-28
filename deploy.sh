#! /bin/bash
set -x
PROJECT_ID=${PROJECT_ID:-"muebles-ra-300d"}
REGION=${REGION:-"us-central1"}
echo "Uploading project $PROJECT_ID"
gcloud config set $PROJECT_ID
gcloud auth configure-docker
gcloud builds submit --config cloudbuild.yaml . # --tag gcr.io/$PROJECT_ID/furniture-repository

echo "Deploying with cloud-run"
gcloud run deploy furniture-repository --image gcr.io/$PROJECT_ID/furniture-repository \
--service-account=muebles-ra-sa@$PROJECT_ID.iam.gserviceaccount.com \
--memory 512M