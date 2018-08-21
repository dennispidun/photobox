FROM ubuntu:16.04
MAINTAINER Dennis Pidun "dennis.pidun@uni-hildesheim.de"

RUN apt-get update && \
    apt-get install -y \
        apt-transport-https \
        bzip2 \
        ca-certificates \
        curl \
        git \
        gtk2-engines-pixbuf \
        lsof \
        moreutils \
        net-tools \
        nano \
        openjdk-8-jdk \
        psmisc \
        software-properties-common \
        unzip \
        wget && \
    rm -rf /var/lib/apt/lists/*

# Install docker-ce
# Specific version because of https://github.com/docker/for-linux/issues/219#issuecomment-375199143
RUN curl -fsSL https://download.docker.com/linux/ubuntu/gpg | apt-key add - && \
    apt-key fingerprint 0EBFCD88 && \
    add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" && \
    apt-get update && \
    apt-get install -y docker-ce=17.09.1~ce-0~ubuntu && \
    rm -rf /var/lib/apt/lists/*

# Install nodejs & yarn
RUN curl -sL https://deb.nodesource.com/setup_8.x | bash - && \
    curl -sS https://dl.yarnpkg.com/debian/pubkey.gpg | apt-key add - && \
    echo "deb https://dl.yarnpkg.com/debian/ stable main" | tee /etc/apt/sources.list.d/yarn.list && \
    apt-get update && \
    apt-get install -y nodejs yarn && \
    rm -rf /var/lib/apt/lists/*

# Install Firefox and Firefox driver
RUN apt-get update && apt-get install -y firefox && \
    rm -rf /var/lib/apt/lists/* && \
    wget https://github.com/mozilla/geckodriver/releases/download/v0.21.0/geckodriver-v0.21.0-linux64.tar.gz && \
    tar -xvzf geckodriver* && \
    chmod +x geckodriver && \
    mv -f geckodriver /usr/local/share/geckodriver && \
    ln -s /usr/local/share/geckodriver /usr/local/bin/geckodriver && \
    ln -s /usr/local/share/geckodriver /usr/bin/geckodriver

# Install Docker-compose
RUN curl -L https://github.com/docker/compose/releases/download/1.18.0/docker-compose-`uname -s`-`uname -m` -o /usr/local/bin/docker-compose && \
    chmod +x /usr/local/bin/docker-compose

VOLUME /var/lib/docker

ENTRYPOINT ["bash"]
