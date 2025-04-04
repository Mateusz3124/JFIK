
class LLVMGenerator {

   static String header_text = "";
   static String main_text = "";
   static int reg = 1;

   static void printf_i32(String id) {
      main_text += "%" + reg + " = load i32, i32* %" + id + "\n";
      reg++;
      main_text += "%" + reg
            + " = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @strpi, i32 0, i32 0), i32 %"
            + (reg - 1) + ")\n";
      reg++;
   }

   static void printf_i64(String id) {
      main_text += "%" + reg + " = load i64, i64* %" + id + "\n";
      reg++;
      main_text += "%" + reg
            + " = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([5 x i8], [5 x i8]* @strpil, i32 0, i32 0), i64 %"
            + (reg - 1) + ")\n";
      reg++;
   }

   static void printf_double(String id) {
      main_text += "%" + reg + " = load double, double* %" + id + "\n";
      reg++;
      main_text += "%" + reg
            + " = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @strpd, i32 0, i32 0), double %"
            + (reg - 1) + ")\n";
      reg++;
   }

   static void scanf_i32(String id) {
      main_text += "%" + reg
            + " = call i32 (i8*, ...) @__isoc99_scanf(i8* getelementptr inbounds ([3 x i8], [3 x i8]* @strsi, i32 0, i32 0), i32* %"
            + id + ")\n";
      reg++;
   }

   static void scanf_i64(String id) {
      main_text += "%" + reg
            + " = call i32 (i8*, ...) @__isoc99_scanf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @strsil, i32 0, i32 0), i64* %"
            + id + ")\n";
      reg++;
   }

   static void scanf_double(String id) {
      main_text += "%" + reg
            + " = call i32 (i8*, ...) @__isoc99_scanf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @strsd, i32 0, i32 0), double* %"
            + id + ")\n";
      reg++;
   }

   static void declare_i32(String id) {
      main_text += "%" + id + " = alloca i32\n";
   }

   static void declare_i64(String id) {
      main_text += "%" + id + " = alloca i64\n";
   }

   static void declare_double(String id) {
      main_text += "%" + id + " = alloca double\n";
   }

   static void assign_i32(String id, String value) {
      main_text += "store i32 " + value + ", i32* %" + id + "\n";
   }

   static void assign_i64(String id, String value) {
      main_text += "store i64 " + value + ", i64* %" + id + "\n";
   }

   static void assign_double(String id, String value) {
      main_text += "store double " + value + ", double* %" + id + "\n";
   }

   // static void load_i32(String id){
   // main_text += "%"+reg+" = load i32, i32* %"+id+"\n";
   // reg++;
   // }

   // static void load_double(String id){
   // main_text += "%"+reg+" = load double, double* %"+id+"\n";
   // reg++;
   // }

   static void add_i32(String val1, String val2) {
      main_text += "%" + reg + " = add i32 " + val1 + ", " + val2 + "\n";
      reg++;
   }

   static void add_i64(String val1, String val2) {
      main_text += "%" + reg + " = add i64 " + val1 + ", " + val2 + "\n";
      reg++;
   }

   static void add_double(String val1, String val2) {
      main_text += "%" + reg + " = fadd double " + val1 + ", " + val2 + "\n";
      reg++;
   }

   static void sub_i32(String val1, String val2) {
      main_text += "%" + reg + " = sub i32 " + val1 + ", " + val2 + "\n";
      reg++;
   }

   static void sub_i64(String val1, String val2) {
      main_text += "%" + reg + " = sub i64 " + val1 + ", " + val2 + "\n";
      reg++;
   }

   static void sub_double(String val1, String val2) {
      main_text += "%" + reg + " = fsub double " + val1 + ", " + val2 + "\n";
      reg++;
   }

   static void mult_i32(String val1, String val2) {
      main_text += "%" + reg + " = mul i32 " + val1 + ", " + val2 + "\n";
      reg++;
   }

   static void mult_i64(String val1, String val2) {
      main_text += "%" + reg + " = mul i64 " + val1 + ", " + val2 + "\n";
      reg++;
   }

   static void mult_double(String val1, String val2) {
      main_text += "%" + reg + " = fmul double " + val1 + ", " + val2 + "\n";
      reg++;
   }

   static void div_i32(String val1, String val2) {
      main_text += "%" + reg + " = div i32 " + val1 + ", " + val2 + "\n";
      reg++;
   }

   static void div_i64(String val1, String val2) {
      main_text += "%" + reg + " = div i64 " + val1 + ", " + val2 + "\n";
      reg++;
   }

   static void div_double(String val1, String val2) {
      main_text += "%" + reg + " = fdiv double " + val1 + ", " + val2 + "\n";
      reg++;
   }

   static void sitofp(String id) {
      main_text += "%" + reg + " = sitofp i32 " + id + " to double\n";
      reg++;
   }

   static void si64tofp(String id) {
      main_text += "%" + reg + " = sitofp i64 " + id + " to double\n";
      reg++;
   }

   static void sext(String id) {
      main_text += "%" + reg + " = sext i32 " + id + " to i64\n";
      reg++;
   }

   static void sext64(String id) {
      main_text += "%" + reg + " = trunc i64 " + id + " to i32\n";
      reg++;
   }

   static void fptosi(String id) {
      main_text += "%" + reg + " = fptosi double " + id + " to i32\n";
      reg++;
   }

   static void fptosi64(String id) {
      main_text += "%" + reg + " = fptosi double " + id + " to i64\n";
      reg++;
   }

   static String generate() {
      String text = "";
      text += "declare i32 @printf(i8*, ...)\n";
      text += "declare i32 @__isoc99_scanf(i8*, ...)\n";
      text += "@strpi = constant [4 x i8] c\"%d\\0A\\00\"\n";
      text += "@strpil = constant [5 x i8] c\"%ld\\0A\\00\"\n";
      text += "@strpd = constant [4 x i8] c\"%f\\0A\\00\"\n";
      text += "@strsi = constant [3 x i8] c\"%d\\00\"\n";
      text += "@strsil = constant [4 x i8] c\"%ld\\00\"\n";
      text += "@strsd = constant [4 x i8] c\"%lf\\00\"\n";
      text += header_text;
      text += "define i32 @main() nounwind{\n";
      text += main_text;
      text += "ret i32 0 }\n";
      return text;
   }

}
