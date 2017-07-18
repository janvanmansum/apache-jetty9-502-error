Vagrant.configure(2) do |config|
   config.vm.define "test" do |test|
      test.vm.box = "geerlingguy/centos6"
      test.vm.hostname = "test"
      test.vm.network :private_network, ip: "192.168.33.32"
      test.vm.network "forwarded_port", guest: 80, host: 8080
      test.vm.provision "shell", path: "install-configure-httpd.sh"
   end
end
