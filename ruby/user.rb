# クラス(オブジェクトの設計図)
# メソッド
# インスタンス
# アクセッサ

class User

    @@count = 0 # クラス変数

    def initialize(name)
        @name = name # インスタンス変数
        @@count += 1
    end

    attr_accessor :name
    attr_reader :name # getter
    attr_writer :name # setter

=begin
    # getter
    def name
       @name
    end

    # setter
    def setName(newName)
        @name = newName
    end
=end

    def sayHi # インスタンスメソッド
        puts "hello, my name is #{@name}"
    end

    def User.sayHello # クラスメソッド
        puts "hello from User class(#{@@count})"
    end

end

# 継承
class SuperUser < User

    def shout
        puts "HELLO!!! from #{@name}"
    end

end

User.sayHello()
wkodate = User.new("wkodate")
ichiro = SuperUser.new("ichiro")
wkodate.sayHi()
ichiro.sayHi()
ichiro.shout()
User.sayHello()
#wkodate.setName("kodate")
wkodate.name = "kodate"
puts wkodate.name
