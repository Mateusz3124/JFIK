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
