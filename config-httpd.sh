#!/usr/bin/env bash

HOST_MACHINE_IP="192.168.33.1"

sudo yum install -y httpd
sudo echo "
<VirtualHost *:80>
  ProxyTimeout 600

  <Location / >
    ProxyPass http://$HOST_MACHINE_IP:20000/
    ProxyPassReverse http://$HOST_MACHINE_IP:20000/
  </Location>
</VirtualHost>
" > /tmp/test.conf
sudo mv /tmp/test.conf /etc/httpd/conf.d/
sudo chkconfig httpd on
sudo service httpd restart
