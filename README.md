apache-jetty9-502-error
=======================

Play back problem scenario: `client ->  Apache HTTP ->  back-end-with-jetty-9`

SYNOPSIS
--------

```
./make-test-file.sh 20000000 # Create a file of 20 million random bytes at data/20000000.bin
vagrant up # Start Apach in a CentOS 6 VM
./server.sh jetty8 # Rebuild and start the back-end server with Jetty 8
./server.sh jetty9 # Rebuild and start the back-end server with Jetty o
./client.sh acceptAll data/20000000.bin # POST the file data/20000000.bin to the back-end server via Apache HTTPD
    # Do not read the data, just send back 201
./client.sh readAll data/20000000.bin # POST the file data/20000000.bin to the back-end server via Apache HTTPD
    # Write data to disk, then send back 201
```

DESCRIPTION
-----------
The purpose of the project is to facilitate playing back a problem that I have when upgrading my back-end service
from Jetty 8 to Jetty 9. I have tried to make this example as minimal as possible, yet to make it easy to start up
as well. Hence the use of Maven, vagrant and VirtualBox. 

The essence of the set-up is:

* Apache HTTP server 2.2.15-60.el6.centos.4
* Back-end service with embedded Jetty 

The Apache server is configured to proxy to the back-end, using the following configuration:

```
<VirtualHost *:80>
  ProxyTimeout 600

  <Location / >
    ProxyPass http://$HOST_MACHINE_IP:20000/
    ProxyPassReverse http://$HOST_MACHINE_IP:20000/
  </Location>
</VirtualHost>
```

where instead of `$HOST_MACHINE_IP` you should fill in the IP address where the back-end is running... 

In my production environment that is localhost, but for the purpose of testing the back-end is running on my Mac and 
the Apache HTTP Server is running inside a VM. Between the VM and the Mac is created a private network by vagrant. The
VM has IP address `192.168.33.32` (configured in `Vagrantfile`) and the Mac `192.168.33.1` (not sure how this is
determined).

# The problem
The problem is that I am getting a `502 Bad Gateway` when `POST`-ing files to this set-up. That is to say: from a certain
size and larger. This only happens with Jetty. It seems to be related to whether the back-end sends a response to Apache
before or after Apache is done sending through the data to the back-end, as the `readAll` servlet works fine, even under
Jetty 9 (this servlet reads all the data that is sent before sending a response).

However, if I want to send back a `401 Unauthorized` because a certain URL requires authorization, I don't want to first
read all the data before sending the 401. In Jetty 8 I didn't have to, but in Jetty 9 this seems to be the only way to 
make it work.

# Inventory of files

| File                      | Purpose
|---------------------------|------------------------------------------------------------------------------------------
| `client.sh`               | POST a file a to servlet with the Apache Commons HTTP Components. You could also use `curl`.
| `config-httpd.sh`         | Run by vagrant on the virtual machine to install and configure Apache HTTP Server
| `make-test-file.sh`       | Create a test file with random content (of specified length) and put it in `data/`.
| `server.sh`               | Recompile the code and start the BackendServer. (Stop with Ctrl-C.) 
| `pom.xml`                 | Maven project file
| `Vagrantfile`             | Configuration file for the VM to be started by vagrant


BUILDING AND RUNNING
--------------------
To build and run these examples you will need the following software:

The versions I use are in parentheses, but I think slightly different versions will work as well.
* Java (8)
* Maven (3.3.9)
* Vagrant (1.9.4)
* VirtualBox (5.1.22)
* The vagrant-vbguest plugin to sync the VirtualBox Guest Additions with the box used in the
  example. Install with `vagrant plugin install vagrant-vbguest`. However if you happend to
  have VirtualBox 5.1.20 then this should not be necessary.
* Git (2.10.0) - to clone the project.

EXAMPLE SESSIONS
----------------
```bash
$ vagrant up
Bringing machine 'test' up with 'virtualbox' provider...
==> test: Importing base box 'geerlingguy/centos6'...
==> test: Matching MAC address for NAT networking...
... more output ...
==> test: Starting httpd:
==> test: httpd: Could not reliably determine the server's fully qualified domain name, using 127.0.0.1 for ServerName
==> test: [  OK  ]
$ ./server.sh
[INFO] Scanning for projects...
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] Building Apache HTTP / Jetty 9 problem minimal example 1.0.0-SNAPSHOT
[INFO] ------------------------------------------------------------------------ 
... more output ...
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 1.535 s
[INFO] Finished at: 2017-07-18T13:47:58+02:00
[INFO] Final Memory: 23M/309M
[INFO] ------------------------------------------------------------------------
2017-07-18 13:47:58.918:INFO::main: Logging initialized @118ms to org.eclipse.jetty.util.log.StdErrLog
2017-07-18 13:47:58.974:INFO:oejs.Server:main: jetty-9.4.6.v20170531
2017-07-18 13:47:59.045:INFO:oejsh.ContextHandler:main: Started o.e.j.s.ServletContextHandler@33e5ccce{/,null,AVAILABLE}
2017-07-18 13:47:59.072:INFO:oejs.AbstractConnector:main: Started ServerConnector@282ba1e{HTTP/1.1,[http/1.1]}{0.0.0.0:20000}
2017-07-18 13:47:59.073:INFO:oejs.Server:main: Started @275ms
```

Now in a different terminal window...
```bash
$ ./make-test-file.sh 10
$ ./make-test-file.sh 1000000
$ ls data/
10.bin       1000000.bin
$ ./client.sh acceptAll data/10.bin
Response status = HTTP/1.1 201 Created
$ ./client.sh acceptAll data/1000000.bin
Response status = HTTP/1.1 502 Bad Gateway
```

When I repeated the above for Jetty 8 there was *no* error.
