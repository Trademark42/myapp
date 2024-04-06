# Use an official Ubuntu runtime as a parent image
FROM ubuntu:latest

# Set the environment variables
ENV ANDROID_SDK_ROOT /opt/android-sdk
ENV PATH ${PATH}:${ANDROID_SDK_ROOT}/cmdline-tools/latest/bin:${ANDROID_SDK_ROOT}/platform-tools

# Install necessary tools
RUN apt-get update && apt-get install -y \
    openjdk-8-jdk \
    wget \
    unzip \
    git \
    curl

# Download and install Android SDK
RUN mkdir -p ${ANDROID_SDK_ROOT}/cmdline-tools && \
    wget -q https://dl.google.com/android/repository/commandlinetools-linux-7583922_latest.zip && \
    unzip *tools*linux*.zip -d ${ANDROID_SDK_ROOT}/cmdline-tools && \
    mv ${ANDROID_SDK_ROOT}/cmdline-tools/cmdline-tools ${ANDROID_SDK_ROOT}/cmdline-tools/latest && \
    yes | sdkmanager --licenses && \
    sdkmanager --update

# Install Android build tools
RUN sdkmanager "build-tools;30.0.3" "platforms;android-30" "platform-tools"
