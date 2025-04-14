import java.util.Stack;

class LLVMGenerator {

   public static int reg = 1;
   public static String header_text = "";
   public static String main_text = "";
   public static int main_tmp = 1;
   public static String buffer = "";
   static int br = 0;
   static Stack<Integer> brstack = new Stack<Integer>();

   public static void printf_i32(String id) {
      buffer += "%" + reg + " = load i32, i32* " + id + "\n";
      reg++;
      buffer += "%" + reg
            + " = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @strpi, i32 0, i32 0), i32 %"
            + (reg - 1) + ")\n";
      reg++;
   }

   public static void printf_i64(String id) {
      buffer += "%" + reg + " = load i64, i64* " + id + "\n";
      reg++;
      buffer += "%" + reg
            + " = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([5 x i8], [5 x i8]* @strpil, i32 0, i32 0), i64 %"
            + (reg - 1) + ")\n";
      reg++;
   }

   public static void printf_double(String id) {
      buffer += "%" + reg + " = load double, double* " + id + "\n";
      reg++;
      buffer += "%" + reg
            + " = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @strpd, i32 0, i32 0), double %"
            + (reg - 1) + ")\n";
      reg++;
   }

   public static void printf_float64(String id) {
      printf_double(id);
   }

   public static void printf_float32(String id) {
      buffer += "%" + reg + " = load float, float* " + id + "\n";
      reg++;
      buffer += "%" + reg + " = fpext float %" + (reg - 1) + " to double\n"; // convert to double for printf
      reg++;
      buffer += "%" + reg
            + " = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @strpf, i32 0, i32 0), double %"
            + (reg - 1) + ")\n";
      reg++;
   }

   public static void scanf_i32(String id) {
      buffer += "%" + reg
            + " = call i32 (i8*, ...) @__isoc99_scanf(i8* getelementptr inbounds ([3 x i8], [3 x i8]* @strsi, i32 0, i32 0), i32* %"
            + id + ")\n";
      reg++;
   }

   public static void scanf_i64(String id) {
      buffer += "%" + reg
            + " = call i32 (i8*, ...) @__isoc99_scanf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @strsil, i32 0, i32 0), i64* %"
            + id + ")\n";
      reg++;
   }

   public static void scanf_double(String id) {
      buffer += "%" + reg
            + " = call i32 (i8*, ...) @__isoc99_scanf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @strsd, i32 0, i32 0), double* %"
            + id + ")\n";
      reg++;
   }

   public static void scanf_float32(String id) {
      buffer += "%" + reg
            + " = call i32 (i8*, ...) @__isoc99_scanf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @strsf, i32 0, i32 0), float* %"
            + id + ")\n";
      reg++;
   }

   public static void scanf_float64(String id) {
      scanf_double(id);
   }

   public static void declare(String id, String type) {
      buffer += "%" + id + " = alloca " + type + "\n";
   }

   public static void declareGlobal(String id, String type, String defaultVal) {
      header_text += "@" + id + " = global " + type + " " + defaultVal + "\n";
   }

   public static void assign(String id, String value, String type) {
      buffer += "store " + type + " " + value + ", " + type + "* " + id + "\n";
   }

   public static int load(String id, String type) {
      buffer += "%" + reg + " = load " + type + ", " + type + "* " + id + "\n";
      reg++;
      return reg - 1;
   }

   public static void add_i32(String val1, String val2) {
      buffer += "%" + reg + " = add i32 " + val1 + ", " + val2 + "\n";
      reg++;
   }

   public static void add_i64(String val1, String val2) {
      buffer += "%" + reg + " = add i64 " + val1 + ", " + val2 + "\n";
      reg++;
   }

   public static void add_double(String val1, String val2) {
      buffer += "%" + reg + " = fadd double " + val1 + ", " + val2 + "\n";
      reg++;
   }

   public static void sub_i32(String val1, String val2) {
      buffer += "%" + reg + " = sub i32 " + val1 + ", " + val2 + "\n";
      reg++;
   }

   public static void sub_i64(String val1, String val2) {
      buffer += "%" + reg + " = sub i64 " + val1 + ", " + val2 + "\n";
      reg++;
   }

   public static void sub_double(String val1, String val2) {
      buffer += "%" + reg + " = fsub double " + val1 + ", " + val2 + "\n";
      reg++;
   }

   public static void mult_i32(String val1, String val2) {
      buffer += "%" + reg + " = mul i32 " + val1 + ", " + val2 + "\n";
      reg++;
   }

   public static void mult_i64(String val1, String val2) {
      buffer += "%" + reg + " = mul i64 " + val1 + ", " + val2 + "\n";
      reg++;
   }

   public static void mult_double(String val1, String val2) {
      buffer += "%" + reg + " = fmul double " + val1 + ", " + val2 + "\n";
      reg++;
   }

   public static void div_i32(String val1, String val2) {
      buffer += "%" + reg + " = div i32 " + val1 + ", " + val2 + "\n";
      reg++;
   }

   public static void div_i64(String val1, String val2) {
      buffer += "%" + reg + " = div i64 " + val1 + ", " + val2 + "\n";
      reg++;
   }

   public static void div_double(String val1, String val2) {
      buffer += "%" + reg + " = fdiv double " + val1 + ", " + val2 + "\n";
      reg++;
   }

   public static void add_float32(String val1, String val2) {
      buffer += "%" + reg + " = fadd float " + val1 + ", " + val2 + "\n";
      reg++;
   }

   public static void sub_float32(String val1, String val2) {
      buffer += "%" + reg + " = fsub float " + val1 + ", " + val2 + "\n";
      reg++;
   }

   public static void mult_float32(String val1, String val2) {
      buffer += "%" + reg + " = fmul float " + val1 + ", " + val2 + "\n";
      reg++;
   }

   public static void div_float32(String val1, String val2) {
      buffer += "%" + reg + " = fdiv float " + val1 + ", " + val2 + "\n";
      reg++;
   }

   public static void sitofp(String id) {
      buffer += "%" + reg + " = sitofp i32 " + id + " to double\n";
      reg++;
   }

   public static void si64tofp(String id) {
      buffer += "%" + reg + " = sitofp i64 " + id + " to double\n";
      reg++;
   }

   public static void sext(String id) {
      buffer += "%" + reg + " = sext i32 " + id + " to i64\n";
      reg++;
   }

   public static void sext64(String id) {
      buffer += "%" + reg + " = trunc i64 " + id + " to i32\n";
      reg++;
   }

   public static void fptosi(String id) {
      buffer += "%" + reg + " = fptosi double " + id + " to i32\n";
      reg++;
   }

   public static void fptosi64(String id) {
      buffer += "%" + reg + " = fptosi double " + id + " to i64\n";
      reg++;
   }

   public static void fptosi_float32_to_i32(String id) {
      buffer += "%" + reg + " = fptosi float " + id + " to i32\n";
      reg++;
   }

   public static void fptosi_float32_to_i64(String id) {
      buffer += "%" + reg + " = fptosi float " + id + " to i64\n";
      reg++;
   }

   public static void sitofp_i32_to_float32(String id) {
      buffer += "%" + reg + " = sitofp i32 " + id + " to float\n";
      reg++;
   }

   public static void sitofp_i64_to_float32(String id) {
      buffer += "%" + reg + " = sitofp i64 " + id + " to float\n";
      reg++;
   }

   public static void float32_to_float64(String id) {
      buffer += "%" + reg + " = fpext float " + id + " to double\n";
      reg++;
   }

   public static void float64_to_float32(String id) {
      buffer += "%" + reg + " = fptrunc double " + id + " to float\n";
      reg++;
   }

   public static void functionstart(String id) {
      main_text += buffer;
      main_tmp = reg;
      buffer = "define i32 @" + id + "() nounwind {\n";
      reg = 1;
   }

   public static void functionstartType(String id, String type) {
      main_text += buffer;
      main_tmp = reg;
      buffer = "define " + type + " @" + id + "() nounwind {\n";
      reg = 1;
   }

   public static void functionend() {
      buffer += "}\n";
      header_text += buffer;
      buffer = "";
      reg = main_tmp;
   }

   public static void addReturn(String type, String val) {
      buffer = buffer + "ret " + type + " " + val + "\n";
   }

   public static void functionendReturn() {
      buffer += "ret i32 0\n";
      buffer += "}\n";
      header_text += buffer;
      buffer = "";
      reg = main_tmp;
   }

   public static int call(String id, String type) {
      buffer += "%" + reg + " = call " + type + " @" + id + "()\n";
      return reg++;
   }

   static void icmp(String val1, String val2, String type) {
      buffer += "%" + reg + " = icmp eq " + type + " " + val1 + ", " + val2 + "\n";
      reg++;
   }

   static void fcmp(String val1, String val2, String type) {
      buffer += "%" + reg + " = fcmp oeq " + type + " " + val1 + ", " + val2 + "\n";
      reg++;
   }

   static void ifstart() {
      br++;
      buffer += "br i1 %" + (reg - 1) + ", label %true" + br + ", label %false" + br + "\n";
      buffer += "true" + br + ":\n";
      brstack.push(br);
   }

   static void ifend() {
      int b = brstack.pop();
      buffer += "br label %false" + b + "\n";
      buffer += "false" + b + ":\n";
   }

   static void repeatstart(String repetitions) {
      declare(Integer.toString(reg), "i32");
      int counter = reg;
      reg++;
      assign("%" + Integer.toString(counter), "0", "i32");
      br++;
      buffer += "br label %cond" + br + "\n";
      buffer += "cond" + br + ":\n";

      load("%" + Integer.toString(counter), "i32");
      add_i32("%" + (String.valueOf(reg - 1)), "1");
      assign("%" + Integer.toString(counter), "%" + (reg - 1), "i32");

      buffer += "%" + reg + " = icmp slt i32 %" + (reg - 2) + ", " + repetitions + "\n";
      reg++;

      buffer += "br i1 %" + (reg - 1) + ", label %true" + br + ", label %false" + br + "\n";
      buffer += "true" + br + ":\n";
      brstack.push(br);
   }

   static void repeatend() {
      int b = brstack.pop();
      buffer += "br label %cond" + b + "\n";
      buffer += "false" + b + ":\n";
   }

   public static void close_main() {
      main_text += buffer;
   }

   public static String generate() {
      String text = "";
      text += "declare i32 @printf(i8*, ...)\n";
      text += "declare i32 @__isoc99_scanf(i8*, ...)\n";
      text += "@strpi = constant [4 x i8] c\"%d\\0A\\00\"\n";
      text += "@strpil = constant [5 x i8] c\"%ld\\0A\\00\"\n";
      text += "@strpf = constant [4 x i8] c\"%f\\0A\\00\"\n";
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
