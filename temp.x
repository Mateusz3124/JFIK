global z : int
z = 20

fun add : float32 { 
  print z
  z = 8
  x : float32
  x = 5.0f + 2.0f
  return x
}

fun see : void {
  valIf : int = 5
  if (valIf == 5){
    print valIf
    if (1.0 == 1.0) {
      print valIf
    }
  }
}

fun loop : void {
  x : int = 1
  for 1 + x {
    for 1 + x {
      print x
    }
  }
}

zet : float32 = 5.0f
zet = add() 
print zet
print z
see()
loop()

x : any = 4
print x
x = 4.0f
print x

struct x {
  val : int
  zen : float32
}

v : struct = struct x
v.val = 4
v.val = 4 + v.val
v.zen = 10.0f / 2.0f
print v.val
print v.zen


class Klasa {
  x : int
  fun see : int {
      print this.x
      return this.x
  }
}

klasa : class Klasa
klasa.x = 4
print klasa.x
klasa.see()