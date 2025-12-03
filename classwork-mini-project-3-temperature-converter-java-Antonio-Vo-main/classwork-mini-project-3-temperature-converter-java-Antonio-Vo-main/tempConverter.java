import java.util.*;

public class tempConverter {
    public static void main(String[] args) {
        System.out.println("F = Fahrenheit, C = Celsius, K = Kelvin. Use FC, CF, CK, KC, KF, or FK for conversions. The first letter is the input unit; the second is the target. Example: Conversion: FK | Value A: 32");
        while (true) {
            
        
        System.out.println("Enter conversion: ");
        Scanner input = new Scanner(System.in);
        String conversion = input.nextLine();
        System.out.println("Enter temperature ");
        double temperature = Double.parseDouble(input.nextLine());
        double result = 0;
        if (conversion.equals("FC")) {
            if (temperature < -459.67) {
                System.out.println("IMPOSABLE VAULE!");
                
            }
            result = (temperature - 32) * 5.0 / 9.0;
        }else if(conversion.equals("CF")) {
            if (result < -459.67) {
                System.out.println("IMPOSABLE VAULE!");
                
            }
            result = (temperature * 9/5) + 32;
        }else if(conversion.equals("CK")) {
            result = temperature + 273.15;
            if (result < 0) {
                System.out.println("IMPOSABLE VAULE!");
                
            }
        }else if(conversion.equals("KC")) {
            if (temperature < 0) {
                System.out.println("IMPOSABLE VAULE!");
                
            }
            result = temperature - 273.15;
        }else if(conversion.equals("KF")) {
            if (temperature < 0) {
                System.out.println("IMPOSABLE VAULE!");
                
            }
            result = (temperature - 273.15) * 9/5 + 32;
        }else if(conversion.equals("FK")) {
            result = (temperature - 32) * 5.0 / 9.0 + 273.15;
            if (result < 0) {
                System.out.println("IMPOSABLE VAULE!");
                
            }
        } else {
            System.out.println("Invaild. Did you input FC, CF, CK, KC, KF, or FK?");
        }
    System.out.println("Result is " + Math.round(result * 1000.0) / 1000.0 + " degrees");
}
}
}