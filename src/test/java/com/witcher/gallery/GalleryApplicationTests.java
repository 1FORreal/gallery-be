package com.witcher.gallery;

import com.witcher.gallery.repositories.PhotoRepository;
import com.witcher.gallery.services.PhotoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.BDDMockito.given;

@SpringBootTest
class GalleryApplicationTests {

	@MockBean
	private PhotoRepository photoRepository;

	@Autowired
	private PhotoService photoService;

	@Test
	void contextLoads() {
	}

}
