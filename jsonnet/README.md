Jsonnet tutorial
===

https://jsonnet.org/learning/tutorial.html

- [Syntax](#syntax)
  - 構文チェック
- [Variables](#variables)
  - 変数埋め込み
- [References](#references)
  - 存在するオブジェクトを参照して再利用
- [Arithmetic](#arithmetic)
  - 数値計算
- [Functions](#functions)
  - 関数を使える
- [Conditionals](#conditionals)
- [Computed Field Names](#computed-field-names)
  - if文も使える
  - 条件に合わせてフィールドを追加
- [Array and Object Comprehension](#array-and-object-comprehension)
  - 動的に配列やオブジェクトを生成
- [Imports](#imports)
  - ファイルやライブラリをimportできる

## Syntax

Convert [syntax.jsonnet](syntax.jsonnet) to output.json.

```
{
   "cocktails": {
      "Manhattan": {
         "description": "A clear \\ red drink.",
         "garnish": "Maraschino Cherry",
         "ingredients": [
            {
               "kind": "Rye",
               "qty": 2.5
            },
            {
               "kind": "Sweet Red Vermouth",
               "qty": 1
            },
            {
               "kind": "Angostura",
               "qty": "dash"
            }
         ],
         "served": "Straight Up"
      },
      "Tom Collins": {
         "description": "The Tom Collins is essentially gin and\nlemonade.  The bitters add complexity.\n",
         "garnish": "Maraschino Cherry",
         "ingredients": [
            {
               "kind": "Farmer's Gin",
               "qty": 1.5
            },
            {
               "kind": "Lemon",
               "qty": 1
            },
            {
               "kind": "Simple Syrup",
               "qty": 0.5
            },
            {
               "kind": "Soda",
               "qty": 2
            },
            {
               "kind": "Angostura",
               "qty": "dash"
            }
         ],
         "served": "Tall"
      }
   }
}
```

## Variables

Convert [variables.jsonnet](variables.jsonnet) to output.json.
```
{
   "Daiquiri": {
      "ingredients": [
         {
            "kind": "Banks Rum",
            "qty": 1.5
         },
         {
            "kind": "Lime",
            "qty": 1
         },
         {
            "kind": "Simple Syrup",
            "qty": 0.5
         }
      ],
      "served": "Straight Up"
   },
   "Mojito": {
      "garnish": "Lime wedge",
      "ingredients": [
         {
            "action": "muddle",
            "kind": "Mint",
            "qty": 6,
            "unit": "leaves"
         },
         {
            "kind": "Banks Rum",
            "qty": 1.5
         },
         {
            "kind": "Lime",
            "qty": 0.5
         },
         {
            "kind": "Simple Syrup",
            "qty": 0.5
         },
         {
            "kind": "Soda",
            "qty": 3
         }
      ],
      "served": "Over crushed ice"
   }
}
```

## References

Convert [references.jsonnet](references.jsonnet) to output.json.

```
{
   "Gin Martini": {
      "garnish": "Olive",
      "ingredients": [
         {
            "kind": "Farmer's Gin",
            "qty": 2
         },
         {
            "kind": "Dry White Vermouth",
            "qty": 1
         }
      ],
      "served": "Straight Up"
   },
   "Martini": {
      "garnish": "Olive",
      "ingredients": [
         {
            "kind": "Farmer's Gin",
            "qty": 2
         },
         {
            "kind": "Dry White Vermouth",
            "qty": 1
         }
      ],
      "served": "Straight Up"
   },
   "Tom Collins": {
      "garnish": "Maraschino Cherry",
      "ingredients": [
         {
            "kind": "Farmer's Gin",
            "qty": 1.5
         },
         {
            "kind": "Lemon",
            "qty": 1
         },
         {
            "kind": "Simple Syrup",
            "qty": 0.5
         },
         {
            "kind": "Soda",
            "qty": 2
         },
         {
            "kind": "Angostura",
            "qty": "dash"
         }
      ],
      "served": "Tall"
   }
}
```

## Arithmetic

Convert [arith.jsonnet](arith.jsonnet) to output.json.

```
{
   "concat_array": [
      1,
      2,
      3,
      4
   ],
   "concat_string": "1234",
   "equality1": false,
   "equality2": true,
   "ex1": 1.6666666666666665,
   "ex2": 3,
   "ex3": 1.6666666666666665,
   "ex4": true,
   "obj": {
      "a": 1,
      "b": 3,
      "c": 4
   },
   "obj_member": true,
   "str1": "The value of self.ex2 is 3.",
   "str2": "The value of self.ex2 is 3.",
   "str3": "ex1=1.67, ex2=3.00",
   "str4": "ex1=1.67, ex2=3.00",
   "str5": "ex1=1.67\nex2=3.00\n"
}
```

## Functions

Convert [functions.jsonnet](functions.jsonnet) to output.json.

```
{
   "call": 12,
   "call_inline_function": 25,
   "call_method1": 9,
   "call_multiline_function": [
      8,
      9
   ],
   "len": [
      5,
      3
   ],
   "named_params": 12,
   "named_params2": 5,
   "standard_lib": "foo bar"
}

```

## Conditionals

Convert [conditionals.jsonnet](conditionals.jsonnet) to output.json.

```
{
   "Large Mojito": {
      "garnish": "Lime wedge",
      "ingredients": [
         {
            "action": "muddle",
            "kind": "Mint",
            "qty": 12,
            "unit": "leaves"
         },
         {
            "kind": "Banks",
            "qty": 3
         },
         {
            "kind": "Lime",
            "qty": 1
         },
         {
            "kind": "Simple Syrup",
            "qty": 1
         },
         {
            "kind": "Soda",
            "qty": 6
         }
      ],
      "served": "Over crushed ice"
   },
   "Mojito": {
      "garnish": null,
      "ingredients": [
         {
            "action": "muddle",
            "kind": "Mint",
            "qty": 6,
            "unit": "leaves"
         },
         {
            "kind": "Banks",
            "qty": 1.5
         },
         {
            "kind": "Lime",
            "qty": 0.5
         },
         {
            "kind": "Simple Syrup",
            "qty": 0.5
         },
         {
            "kind": "Soda",
            "qty": 3
         }
      ],
      "served": "Over crushed ice"
   },
   "Virgin Mojito": {
      "garnish": null,
      "ingredients": [
         {
            "action": "muddle",
            "kind": "Mint",
            "qty": 6,
            "unit": "leaves"
         },
         {
            "kind": "Lime",
            "qty": 0.5
         },
         {
            "kind": "Simple Syrup",
            "qty": 0.5
         },
         {
            "kind": "Soda",
            "qty": 3
         }
      ],
      "served": "Over crushed ice"
   }
}
```

## Computed Field Names 

Convert [computed-fields.jsonnet](computed-fields.jsonnet) to output.json.

```
{
   "Margarita": {
      "garnish": "Salt",
      "ingredients": [
         {
            "kind": "Tequila Blanco",
            "qty": 2
         },
         {
            "kind": "Lime",
            "qty": 1
         },
         {
            "kind": "Cointreau",
            "qty": 1
         }
      ]
   },
   "Margarita Unsalted": {
      "ingredients": [
         {
            "kind": "Tequila Blanco",
            "qty": 2
         },
         {
            "kind": "Lime",
            "qty": 1
         },
         {
            "kind": "Cointreau",
            "qty": 1
         }
      ]
   }
}
```

## Array and Object Comprehension

Convert [comprehensions.jsonnet](comprehensions.jsonnet) to output.json.

```
{
   "array_comprehensions": {
      "evens": [
         6,
         8
      ],
      "evens_and_odds": [
         "6-5",
         "6-7",
         "8-5",
         "8-7"
      ],
      "higher": [
         8,
         9,
         10,
         11
      ],
      "lower": [
         2,
         3,
         4,
         5
      ],
      "odds": [
         5,
         7
      ]
   },
   "object_comprehensions": {
      "evens": {
         "f6": true,
         "f8": true
      },
      "mixture": {
         "a": 0,
         "b": 0,
         "c": 0,
         "f": 1,
         "g": 2
      }
   }
}
```

## Imports

Convert [imports.jsonnet](imports.jsonnet) with [martinis.libsonnet](martinis.libsonnet) and [garnish.txt](garnish.txt) to output.json.

```
{
   "Manhattan": {
      "garnish": "Maraschino Cherry\n",
      "ingredients": [
         {
            "kind": "Rye",
            "qty": 2.5
         },
         {
            "kind": "Sweet Red Vermouth",
            "qty": 1
         },
         {
            "kind": "Angostura",
            "qty": "dash"
         }
      ],
      "served": "Straight Up"
   },
   "Vodka Martini": {
      "garnish": "Olive",
      "ingredients": [
         {
            "kind": "Vodka",
            "qty": 2
         },
         {
            "kind": "Dry White Vermouth",
            "qty": 1
         }
      ],
      "served": "Straight Up"
   }
}
```