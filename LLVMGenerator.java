import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class LLVMGenerator {

   public static int reg = 1;
   public static String header_text = "";
   public static String main_text = "";
   public static Set<String> free_text = new HashSet<String>();
   public static int main_tmp = 1;
   public static String buffer = "";
   static int br = 0;
   static Stack<Integer> brstack = new Stack<Integer>();

   public static void printf_i32(String id) {
      buffer += "%" + reg
            + " = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @strpi, i32 0, i32 0), i32 %"
            + (reg - 1) + ")\n";
      reg++;
   }

   public static void printf_i64(String id) {
      buffer += "%" + reg
            + " = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([5 x i8], [5 x i8]* @strpil, i32 0, i32 0), i64 %"
            + (reg - 1) + ")\n";
      reg++;
   }

   public static void printf_double(String id) {
      buffer += "%" + reg
            + " = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @strpd, i32 0, i32 0), double %"
            + (reg - 1) + ")\n";
      reg++;
   }

   public static void printf_float64(String id) {
      printf_double(id);
   }

   public static void printf_float32(String id) {
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

   public static void malloc(String id, int typeSize) {
      buffer += "%" + reg + "= call ptr @malloc(i64 " + String.valueOf(typeSize) + ")" + "\n";
      buffer += "store ptr %" + reg + ", ptr " + id + "\n";
      reg++;
   }

   public static void assignAny(String id, String type, String val, int typeSize) {
      malloc(id, typeSize);
      buffer += "%" + reg + " = load ptr, ptr " + id + "\n";
      buffer += "store " + type + " " + val + ", ptr %" + reg + "\n";
      reg++;
      free_text.add(id);
   }

   public static void assignStruct(String type, String val, String id) {
      buffer += "store " + type + " " + val + ", ptr " + id + "\n";
   }

   public static void assignPtrPtr(String id1, String id2) {
      buffer += "store ptr " + id1 + ", ptr " + id2 + "\n";
   }

   public static void assign(String id, String value, String type) {
      buffer += "store " + type + " " + value + ", " + type + "* " + id + "\n";
   }

   public static void free(String id) {
      buffer += "%" + reg + " = load ptr, ptr " + id + "\n";
      buffer += "call void @free(ptr noundef %" + reg + ")\n";
      reg++;
   }

   public static void freeAtEnd() {
      for (String val : free_text) {
         free(val);
      }
   }

   public static int load(String id, String type) {
      buffer += "%" + reg + " = load " + type + ", " + type + "* " + id + "\n";
      reg++;
      return reg - 1;
   }

   public static int loadPtr(String id) {
      buffer += "%" + reg + " = load ptr, ptr " + id + "\n";
      reg++;
      return reg - 1;
   }

   public static int loadPtrToType(String id, String type) {
      buffer += "%" + reg + " = load " + type + ", ptr " + id + "\n";
      reg++;
      return reg - 1;
   }

   public static int loadStruct(String id, String struct, int keyIdx) {
      buffer += "%" + reg + " = load ptr, ptr " + id + "\n";
      reg++;
      buffer += "%" + reg + " = getelementptr inbounds %struct." + struct + ", ptr %" + (reg - 1) + ", i32 0, i32 "
            + keyIdx + "\n";
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

   public static void functionstartType(String id, String type, String args) {
      main_text += buffer;
      main_tmp = reg;
      buffer = "define " + type + " @" + id + "(" + args + ") nounwind {\n";
      reg = 1;
   }

   public static void initThis() {
      buffer += "%this = alloca ptr\n";
      buffer += "store ptr %0, ptr %this\n";
      reg++;
   }

   public static void addReturn(String type, String val) {
      buffer = buffer + "ret " + type + " " + val + "\n";
   }

   public static void functionend() {
      buffer += "}\n";
      header_text += buffer;
      buffer = "";
      reg = main_tmp;
   }

   public static void functionendReturn() {
      buffer += "ret i32 0\n";
      buffer += "}\n";
      header_text += buffer;
      buffer = "";
      reg = main_tmp;
   }

   private static int CountSubstring(String input, String target) {
      int count = 0;
      int index = 0;

      while ((index = input.indexOf(target, index)) != -1) {
         count++;
         index += target.length();
      }

      return count;
   }

   private static String changeToGenerator(String buffer, String name, String type) {
      buffer = "@" + name + "_iterator" + " = global i32 0\n" + buffer;
      int returnCount = CountSubstring(buffer, "\nret");
      String[] parts = buffer.split("\\{\\n");
      parts[0] += "{\n%loaded_iterator = load i32, i32* @" + name + "_iterator" + "\n";
      parts[0] += "switch i32 %loaded_iterator, label %default [\n";
      String[] labels = new String[returnCount];
      for (int i = 0; i < returnCount; i++) {
         parts[0] += "i32 " + i + ", label %label_" + i + "\n";
         labels[i] = "label_" + i;
      }
      parts[0] += "]\n";
      parts[0] += labels[0] + ":    ; preds = %0\n";
      buffer = parts[0] + parts[1];
      String[] lines = buffer.split("\n");

      StringBuilder result = new StringBuilder();

      int counter = 1;
      for (String line : lines) {
         if (line.startsWith("ret") && counter < returnCount) {
            line = line + "\n" + labels[counter] + ":    ; preds = %" + counter;
            counter++;
         }
         result.append(line).append("\n");
      }
      buffer = result.toString();
      buffer = buffer.substring(0, buffer.length() - 2);
      buffer += "default: ; preds = %-1\n";
      if (type.equals("i32") || type.equals("i64")) {
         buffer += "ret " + type + " 0\n";
      } else {
         buffer += "ret " + type + " 0.0\n";
      }
      buffer += "}\n";
      return buffer;
   }

   public static void generatorIterate(String name) {
      String variableName = "@" + name + "_iterator";
      int currReg = load(variableName, "i32");
      add_i32("%" + currReg, "1");
      assign(variableName, "%" + (reg - 1), "i32");
   }

   public static void generatorend(String name, String type) {
      buffer += "}\n";
      buffer = changeToGenerator(buffer, name, type);
      // System.err.println(buffer);
      // System.exit(2);
      header_text += buffer;
      buffer = "";
      reg = main_tmp;
   }

   public static void generatorendReturn(String name) {
      buffer += "ret i32 0\n";
      buffer += "}\n";
      buffer = changeToGenerator(buffer, name, "i32");
      // System.err.println(buffer);
      // System.exit(2);
      header_text += buffer;
      buffer = "";
      reg = main_tmp;
   }

   public static int call(String id, String type) {
      buffer += "%" + reg + " = call " + type + " @" + id + "()\n";
      return reg++;
   }

   public static int call(String id, String type, String args) {
      buffer += "%" + reg + " = call " + type + " @" + id + "(" + args + ")\n";
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

   public static void structInit(String id, String values) {
      header_text += "%struct." + id + " = type { " + values + " }\n";
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
      text += "declare ptr @malloc(i64)\n";
      text += "declare void @free(ptr noundef)\n";
      return text;
   }

}
