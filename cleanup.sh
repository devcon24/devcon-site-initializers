#!/bin/bash

CURRENT_DIR="$(pwd)"
LIFERAY_HOME="${CURRENT_DIR}/bundles"

TEMP_DIRS=(
    "${LIFERAY_HOME}/logs"
    "${LIFERAY_HOME}/osgi/state"
    "${LIFERAY_HOME}/work"
    "${LIFERAY_HOME}/tomcat/logs"
    "${LIFERAY_HOME}/tomcat/temp"
    "${LIFERAY_HOME}/tomcat/work"
)

TEMP_FILES=(
  "${CURRENT_DIR}/build"
  "${CURRENT_DIR}/node_modules"
  "${CURRENT_DIR}/node_modules_cache"
  "${CURRENT_DIR}/.yarnrc"
  "${CURRENT_DIR}/package.json"
  "${CURRENT_DIR}/package-lock.json"
  "${CURRENT_DIR}/yarn.lock"
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

for TEMP_FILE in "${TEMP_FILES[@]}"; do
    if [ -e "${TEMP_FILE}" ]; then
      echo "Removing ${TEMP_FILE}..."
      rm -rf "${TEMP_FILE:?}"
    else
        echo "${TEMP_FILE} does not exist."
    fi
done