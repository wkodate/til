class Task < ActiveRecord::Base
  belongs_to :project
  validates :title, presence: true
  scope :unfinished, -> { where(done: false) }
end
