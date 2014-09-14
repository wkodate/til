require 'spec_helper'

describe command('whoami') do
  it { should return_stdout 'vagrant'}
end
