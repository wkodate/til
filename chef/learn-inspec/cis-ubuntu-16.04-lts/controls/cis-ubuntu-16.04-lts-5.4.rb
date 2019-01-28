control 'cis-ubuntu-lts-5.4.4' do
  impact 0.7
  title 'Ensure default user umask is 027 or more restrictive'
  desc 'The default umask determines the permissions of files created by users.'
  describe file('/etc/bash.bashrc') do
    its('content') { should match /^umask 027/ }
  end
  describe file('/etc/profile') do
    its('content') { should match /^umask 027/ }
  end
end
