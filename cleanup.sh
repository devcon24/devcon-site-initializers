#!/bin/bash

LIFERAY_HOME="/home/vitaliy/Work/Projects/pdmfc/cyprus-educ/moesy-sms/bundles"

TEMP_DIRS=(
    "${LIFERAY_HOME}/logs"
    "${LIFERAY_HOME}/osgi/state"
    "${LIFERAY_HOME}/work"
    "${LIFERAY_HOME}/tomcat-9.0.83/logs"
    "${LIFERAY_HOME}/tomcat-9.0.83/temp"
    "${LIFERAY_HOME}/tomcat-9.0.83/work"
)

cleanup_dir() {
    DIR_PATH=$1
    if [ -d "${DIR_PATH}" ]; then
        echo "Cleaning up ${DIR_PATH}..."
        rm -rf "${DIR_PATH:?}"/*
        echo "${DIR_PATH} cleanup done."
    else
        echo "Directory ${DIR_PATH} does not exist."
    fi
}

for DIR in "${TEMP_DIRS[@]}"; do
    cleanup_dir "${DIR}"
done