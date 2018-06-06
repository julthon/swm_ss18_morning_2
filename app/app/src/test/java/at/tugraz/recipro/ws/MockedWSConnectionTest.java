package at.tugraz.recipro.ws;

import android.graphics.Bitmap;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.robolectric.shadows.ShadowLog;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class, WSConnection.class})
public class MockedWSConnectionTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private WSConnection wsConnection;

    @Before
    public void setUp() {
        ShadowLog.stream = System.out;
        this.wsConnection = WSConnection.getInstance();
        this.wsConnection.init("https://aughrany.com/recipro-backend/api", true);
        PowerMockito.mockStatic(Log.class);
    }

    @Test
    public void testPostImageFailure() throws Exception {
        byte[] image = IOUtils.toByteArray(this.getClass().getClassLoader().getResource("taco.jpeg"));

        RestTemplate restTemplate = PowerMockito.mock(RestTemplate.class);
        ResponseEntity<Void> response = Mockito.mock(ResponseEntity.class);

        when(response.getStatusCode()).thenReturn(HttpStatus.I_AM_A_TEAPOT);
        when(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(HttpEntity.class), eq(Void.class))).thenReturn(response);
        PowerMockito.whenNew(RestTemplate.class).withNoArguments().thenReturn(restTemplate);

        boolean result = this.wsConnection.postImage(1, image, ImageType.JPEG);
        assertFalse(result);
    }

    @Test
    public void testGetInvalidImage() throws Exception {
        RestTemplate restTemplate = PowerMockito.mock(RestTemplate.class);
        ResponseEntity<Resource> response = Mockito.mock(ResponseEntity.class);

        when(response.getStatusCode()).thenReturn(HttpStatus.I_AM_A_TEAPOT);
        when(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(HttpEntity.class), eq(Resource.class))).thenThrow(IOException.class);
        PowerMockito.whenNew(RestTemplate.class).withNoArguments().thenReturn(restTemplate);

        Bitmap bitmap = this.wsConnection.getImage(1);
        assertNull(bitmap);
    }
}
