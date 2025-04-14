declare noalias i8* @malloc(i64 noundef) #3
declare i32 @printf(i8* noundef, ...)

@.str = private unnamed_addr constant [4 x i8] c"%d\0A\00", align 1

define dso_local i32 @main() #0 {
entry:
  %1 = alloca ptr
  %2 = call ptr @malloc(i64 4)
  store ptr %2, ptr %1
  store i32 4, ptr %1
  %5 = load i32, ptr %1
  %6 = call i32 (ptr, ...) @printf(ptr @.str, i32 %5)
  ret i32 0
}
