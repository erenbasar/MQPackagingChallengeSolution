import com.mobiquity.exception.APIException;
import com.mobiquity.packer.Packer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

public class PackagingTest {

    @Test
    public void testExceptionCostFormat() throws FileNotFoundException, APIException {
        Assertions.assertThrows(APIException.class, () -> {
            Packer.pack("src/main/test/resources/ExceptionCostFormat.txt");
        });
    }

    @Test
    public void testExceptionItemLimit() throws FileNotFoundException, APIException {
        Assertions.assertThrows(APIException.class, () -> {
            Packer.pack( "src/main/test/resources/ExceptionItemLimit.txt");
        });
    }

    @Test
    public void testExceptionMaxCost() throws FileNotFoundException, APIException {
        Assertions.assertThrows(APIException.class, () -> {
            Packer.pack("src/main/test/resources/ExceptionMaxCost.txt");
        });
    }

    @Test
    public void testExceptionMaxWeight() throws FileNotFoundException, APIException {
        Assertions.assertThrows(APIException.class, () -> {
            Packer.pack("src/main/test/resources/ExceptionMaxWeight.txt");
        });
    }

    @Test
    public void testExceptionPackageLimit() throws FileNotFoundException, APIException {
        Assertions.assertThrows(APIException.class, () -> {
            Packer.pack("src/main/test/resources/ExceptionPackageLimit.txt");
        });
    }

    @Test
    public void testExceptionWeightFormat() throws FileNotFoundException, APIException {
        Assertions.assertThrows(APIException.class, () -> {
            Packer.pack("src/main/test/resources/ExceptionWeightFormat.txt");
        });
    }

    @Test
    public void testWrongPath() throws FileNotFoundException, APIException {
        Assertions.assertThrows(APIException.class, () -> {
            Packer.pack("src/main/test/resources/xxx.txt");
        });
    }

    @Test
    public void testTrueFormatItems() throws IOException, APIException {
        String result;
        result =Packer.pack("src/main/test/resources/TrueFormatItems.txt");
        System.out.println(result);
    }

}
