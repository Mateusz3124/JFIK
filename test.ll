declare i32 @printf(i8*, ...)
declare i32 @__isoc99_scanf(i8*, ...)
@strpi = constant [4 x i8] c"%d\0A\00"
@strpil = constant [5 x i8] c"%ld\0A\00"
@strpf = constant [4 x i8] c"%f\0A\00"
@strpd = constant [4 x i8] c"%f\0A\00"
@strsi = constant [3 x i8] c"%d\00"
@strsil = constant [4 x i8] c"%ld\00"
@strsd = constant [4 x i8] c"%lf\00"
@z = global ptr null
define float @add() nounwind {
%1 = load ptr, ptr @z
%2 = load i32, i32* %1
%3 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @strpi, i32 0, i32 0), i32 %2)
%4 = load ptr, ptr @z
call void @free(ptr noundef %4)
%5= call ptr @malloc(i64 4)
store ptr %5, ptr @z
%6 = load ptr, ptr @z
store float 8.0, ptr %6
%x = alloca float
%7 = fadd float 5.0, 2.0
store float %7, float* %x
%8 = load float, float* %x
ret float %8
}
define i32 @see() nounwind {
%valIf = alloca i32
store i32 5, i32* %valIf
%1 = load i32, i32* %valIf
%2 = icmp eq i32 5, %1
br i1 %2, label %true1, label %false1
true1:
%3 = load i32, i32* %valIf
%4 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @strpi, i32 0, i32 0), i32 %3)
%5 = fcmp oeq double 1.0, 1.0
br i1 %5, label %true2, label %false2
true2:
%6 = load i32, i32* %valIf
%7 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @strpi, i32 0, i32 0), i32 %6)
br label %false2
false2:
br label %false1
false1:
ret i32 0
}
define i32 @loop() nounwind {
%x = alloca i32
store i32 1, i32* %x
%1 = load i32, i32* %x
%2 = add i32 1, %1
%3 = alloca i32
store i32 0, i32* %3
br label %cond3
cond3:
%4 = load i32, i32* %3
%5 = add i32 %4, 1
store i32 %5, i32* %3
%6 = icmp slt i32 %4, %2
br i1 %6, label %true3, label %false3
true3:
%7 = load i32, i32* %x
%8 = add i32 1, %7
%9 = alloca i32
store i32 0, i32* %9
br label %cond4
cond4:
%10 = load i32, i32* %9
%11 = add i32 %10, 1
store i32 %11, i32* %9
%12 = icmp slt i32 %10, %8
br i1 %12, label %true4, label %false4
true4:
%13 = load i32, i32* %x
%14 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @strpi, i32 0, i32 0), i32 %13)
br label %cond4
false4:
br label %cond3
false3:
ret i32 0
}
%struct.x = type { i32, float }
define i32 @main() nounwind{
%1= call ptr @malloc(i64 4)
store ptr %1, ptr @z
%2 = load ptr, ptr @z
store i32 20, ptr %2
%zet = alloca float
store float 5.0, float* %zet
%3 = call float @add()
store float %3, float* %zet
%4 = load ptr, ptr @z
%5 = load float, float* %4
%6 = fpext float %5 to double
%7 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @strpf, i32 0, i32 0), double %6)
%8 = load float, float* %zet
%9 = fpext float %8 to double
%10 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @strpf, i32 0, i32 0), double %9)
%11 = call i32 @see()
%12 = call i32 @loop()
%x = alloca ptr
%13= call ptr @malloc(i64 4)
store ptr %13, ptr %x
%14 = load ptr, ptr %x
store i32 4, ptr %14
%15 = load ptr, ptr %x
%16 = load i32, i32* %15
%17 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @strpi, i32 0, i32 0), i32 %16)
%18 = load ptr, ptr %x
call void @free(ptr noundef %18)
%19= call ptr @malloc(i64 4)
store ptr %19, ptr %x
%20 = load ptr, ptr %x
store float 4.0, ptr %20
%21 = load ptr, ptr %x
%22 = load float, float* %21
%23 = fpext float %22 to double
%24 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @strpf, i32 0, i32 0), double %23)
%v = alloca ptr
%25= call ptr @malloc(i64 16)
store ptr %25, ptr %v
%26 = load ptr, ptr %v
%27 = getelementptr inbounds %struct.x, ptr %26, i32 0, i32 0
store i32 4, ptr %27
%28 = load ptr, ptr %v
%29 = getelementptr inbounds %struct.x, ptr %28, i32 0, i32 0
%30 = load i32, ptr %29
%31 = add i32 4, %30
%32 = load ptr, ptr %v
%33 = getelementptr inbounds %struct.x, ptr %32, i32 0, i32 0
store i32 %31, ptr %33
%34 = fdiv float 10.0, 2.0
%35 = load ptr, ptr %v
%36 = getelementptr inbounds %struct.x, ptr %35, i32 0, i32 1
store float %34, ptr %36
%37 = load ptr, ptr %v
%38 = getelementptr inbounds %struct.x, ptr %37, i32 0, i32 0
%39 = load i32, ptr %38
%40 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @strpi, i32 0, i32 0), i32 %39)
%41 = load ptr, ptr %v
%42 = getelementptr inbounds %struct.x, ptr %41, i32 0, i32 1
%43 = load float, ptr %42
%44 = fpext float %43 to double
%45 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @strpf, i32 0, i32 0), double %44)
%46 = load ptr, ptr %x
call void @free(ptr noundef %46)
%47 = load ptr, ptr @z
call void @free(ptr noundef %47)
ret i32 0 }
declare ptr @malloc(i64)
declare void @free(ptr noundef)

