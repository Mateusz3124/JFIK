declare i32 @printf(i8*, ...)
declare i32 @__isoc99_scanf(i8*, ...)
@strpi = constant [4 x i8] c"%d\0A\00"
@strpil = constant [5 x i8] c"%ld\0A\00"
@strpf = constant [4 x i8] c"%f\0A\00"
@strpd = constant [4 x i8] c"%f\0A\00"
@strsi = constant [3 x i8] c"%d\00"
@strsil = constant [4 x i8] c"%ld\00"
@strsd = constant [4 x i8] c"%lf\00"
define i32 @main() nounwind{
%we = alloca float
store float 21.0, float* %we
%1 = load float, float* %we
%2 = fpext float %1 to double
%3 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @strpf, i32 0, i32 0), double %2)
%u = alloca i64
%y = alloca double
%4 = fptosi double 3.2 to i64
store i64 %4, i64* %u
%5 = sitofp i64 230 to double
store double %5, double* %y
%6 = load i64, i64* %u
%7 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([5 x i8], [5 x i8]* @strpil, i32 0, i32 0), i64 %6)
%8 = load double, double* %y
%9 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @strpd, i32 0, i32 0), double %8)
%x = alloca i32
%10 = call i32 (i8*, ...) @__isoc99_scanf(i8* getelementptr inbounds ([3 x i8], [3 x i8]* @strsi, i32 0, i32 0), i32* %x)
%11 = load i32, i32* %x
%12 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @strpi, i32 0, i32 0), i32 %11)
%z = alloca double
%13 = fadd double 2.3, 4.2
store double %13, double* %z
%14 = load double, double* %z
%15 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @strpd, i32 0, i32 0), double %14)
%16 = fsub double 3.2, 1.2
store double %16, double* %z
%17 = load double, double* %z
%18 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @strpd, i32 0, i32 0), double %17)
%19 = fdiv double 10.0, 2.0
store double %19, double* %z
%20 = load double, double* %z
%21 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @strpd, i32 0, i32 0), double %20)
%22 = fmul double 4.0, 2.0
%23 = fadd double 2.0, %22
store double %23, double* %z
%24 = load double, double* %z
%25 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @strpd, i32 0, i32 0), double %24)
%h = alloca i64
%26 = call i32 (i8*, ...) @__isoc99_scanf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @strsil, i32 0, i32 0), i64* %h)
%27 = load i64, i64* %h
%28 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([5 x i8], [5 x i8]* @strpil, i32 0, i32 0), i64 %27)
%w = alloca i64
store i64 9223372036854775807, i64* %w
%29 = load i64, i64* %w
%30 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([5 x i8], [5 x i8]* @strpil, i32 0, i32 0), i64 %29)
ret i32 0 }

