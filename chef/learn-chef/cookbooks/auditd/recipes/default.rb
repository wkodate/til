#
# Cookbook:: auditd
# Recipe:: default
#
# Copyright:: 2019, The Authors, All Rights Reserved.

apt_update 'Update the apt cache daily' do
  frequency 86_400
  action :periodic
end

package 'auditd' do
  action :install
end

template '/etc/audit/auditd.conf' do
  source 'auditd.conf.erb'
  notifies :restart, 'service[auditd]'
end

service 'auditd' do
  action :nothing
end
