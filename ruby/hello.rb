print "hello world" # 改行なし
puts "hello(puts)" # 改行がある
p "hello(p)" # データの形式がわかるように表示

=begin
複数行のコメントが
書けます
=end

# 変数: データにつけるラベル
msg = "hello world"
p msg
# 定数: 変更されないデータに付けるラベル。値の変更はできない
ADMIN_EMAIL = "wkodate@mailamail.com"
p ADMIN_EMAIL

# データは全てオブジェクト
# - メソッド(クラスで定義されている)
p "hello world".length

# 数値オブジェクト
x = 10
y = 20.5
z = Rational(1,3)

p x % 3 # 1
p x ** 3 # 1000
p z * 2
# x = x + 5
x += 5 #自己代入
p x # 15
p y.round

# 文字列オブジェクト
name = "wkodate"
x = "hel\tlo\n world, #{name}" # 変数展開、特殊文字 {\n \t}
y = 'hel\tlo\n world #{name}'
puts x
puts y
# + *
puts "hello world" + " wkodate"
puts "hello " * 5
# メソッド
# ! 破壊的メソッド。元データを書き換える
# ? 真偽値を返すメソッド
s = "wkodate"
puts s.upcase #WKODATE
puts s # wkodate
puts s.upcase! #WKODATE
puts s #WKODATE

s = ""
p s.empty? # true

# 配列オブジェクト
# sales_1, sales_2, ...
sales = [5, 8 ,4]
p sales[1] # 8
sales[1] = 10
p sales[1] #10
sales = [5, 8 ,4]
p sales[0..1] # [5,10]
p sales[-1] # 4
sales[0..2] = [1,2]
p sales # [1, 2]
sales[0,2]=[]
p sales # []
sales = [5, 8 ,4]
p sales.sort.reverse # [8, 5, 4]
sales << 100 << 102
p sales # [5, 8, 4, 100, 102]

# ハッシュオブジェクト
sales = {"wkodate" => 200, "ichiro" => 300}
p sales["wkodate"]
sales = {:wkodate => 200, :ichiro => 300}
p sales[:wkodate]
sales = {wkodate: 200, ichiro: 300}
p sales[:wkodate]
p sales.size
p sales.keys
p sales.values
p sales.has_key?(:wkodate)

# オブジェクトの変換
a = 10
b = "5"
p a + b.to_i # bをintegerに変換
p a + b.to_f # bをfloatに変換
p a.to_s + b # aをstringに変換
h = {wkodate: 100, ichiro: 200}
p h.to_a # arrayに変換
p h.to_a.to_h # hashに変換

# %記法
# 文字列の中で区切り文字を使いたいとき
 s = "hel\"lo"
 p s
 s = %Q(hel"lo)
 p s
 s = %(hel"lo)
 p s
 # a = ["a", "b", "c"]
 a = %W(a b c)
 p a
 # a = ['a', 'b', 'c']
 a = %w(a b c)
 p a

=begin
条件分岐 if

if 条件
  真
else
  偽
end

elsif
&& and
|| or
!  not
=end
score = 80
if score > 60
    p "OK"
else
    p "NG"
end

puts "OK" if score > 60

=begin
true: それ以外(0 ''を含む)
false: false nil(オブジェクトが存在しない)
=end
b, c = 10, 20 # 多重代入
a = b > c ? b : c
puts a

=begin
条件分岐 case

case 比較したいオブジェクト
when 値
  処理
when 値
  処理
when 値
  処理
else
  処理
end
=end
signal = "red"
case signal
when "red"
    puts "STOP!"
when "green"
    puts "GO!"
when "yellow"
    puts "CAUTION!"
else
    puts "wrong signal"
end

# 繰り返し処理
# times
# while
3.times do |i|
    puts "#{i}: hello"
end

i = 0
while i < 3 do
    puts "#{i}: hello"
    i += 1
end
# break ループを抜ける
# # next ループを1回スキップ
3.times do |i|
    if i == 1
        # break
        next
    end
    puts "#{i}: hello"
end

# 繰り返し処理
# for 
# each
for i in 0..2 do 
    puts i 
end
for color in ["red", "blue", "pink"] do 
    puts color 
end
["red", "blue", "pink"].each do |color|
    puts color 
end
{"red"=>200, "blue"=>100, "pink"=>50}.each do |color, price|
    puts "#{color}: #{price}"
end

# 関数的メソッド
def sayHi(name)
    #puts "hello! " + name
    s = "hello! " + name
    return s
end
greet = sayHi("wkodate")
puts greet

# Timeクラス
t = Time.now # 現在時刻取得
p t
p t.year
p t.month
t = Time.new(2013, 12, 25, 12, 32, 22)
p t
t += 10 # 四則演算ができる
p t
p t.strftime("Updated: %Y-%m-%d")
p t 
