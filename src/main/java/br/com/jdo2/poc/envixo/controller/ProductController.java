package br.com.jdo2.poc.envixo.controller;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;

import br.com.jdo2.poc.envixo.model.Product;
import br.com.jdo2.poc.envixo.model.ProductFile;
import br.com.jdo2.poc.envixo.repository.ProductFileRepository;
import br.com.jdo2.poc.envixo.repository.ProductRepository;
import br.com.jdo2.poc.envixo.util.IOUtil;
import br.com.jdo2.poc.envixo.util.MediaTypeUtil;
import br.com.jdo2.poc.envixo.view.ErrorView;
import br.com.jdo2.poc.envixo.view.IdResponse;
import br.com.jdo2.poc.envixo.view.PageResponse;
import br.com.jdo2.poc.envixo.view.ProductView;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("products")
public class ProductController {
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ProductFileRepository productFileRepository;

	@Autowired
	private ModelMapper modelMapper;
	
	@Value("${br.com.envixo.s3.bucket}")
	private String s3Bucket;
	@Value("${br.com.envixo.s3.folder}")
	private String s3Folder;
	@Value("${br.com.envixo.s3.accessKey}")
	private String s3AccessKey;
	@Value("${br.com.envixo.s3.secret}")
	private String s3Secret;
	
	@GetMapping
	public ResponseEntity<?> list(@RequestParam(name="name", required = false) String name, @RequestParam(name="status", required = false) String status,
			@RequestParam(name="page", defaultValue = "0") Integer page, 
			@RequestParam(name="size", defaultValue = "10") Integer size, 
			@RequestParam(name="field", defaultValue = "id") String field, 
			@RequestParam(name="direction", defaultValue = "asc") String direction){
		try {
			PageRequest pRequest = PageRequest.of(page, size, Direction.fromString(direction), field);
			PageResponse<ProductView> pResponse = new PageResponse<>();
			Page<Product> pPage = null;
			Product pExample = new Product();
			pExample.setName(name);
			pExample.setStatus(status);
			pPage = productRepository.findAll(Example.of(pExample), pRequest);
			List<ProductView> pList = new ArrayList<>();
			pPage.getContent().stream().forEach(p -> {
				ProductView pV = new ProductView();
				modelMapper.map(p, pV);
				pList.add(pV);
			});
			pResponse.setContent(pList);
			pResponse.setTotalPages(pPage.getTotalPages());
			return ResponseEntity.ok(pResponse);
		}catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new ErrorView(e.getMessage()));
		}
	}

	@GetMapping("{id}")
	public ResponseEntity<?> get(@PathVariable("id") Integer id){
		try {
			Product p = productRepository.getById(id);
			ProductView pV = new ProductView();
			modelMapper.map(p, pV);
			return ResponseEntity.ok(pV);
		}catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new ErrorView(e.getMessage()));
		}
	}
	
	@PostMapping
	public ResponseEntity<?> insert(@RequestBody ProductView product){
		try {
			Product p = new Product();
			modelMapper.map(product, p);
			productRepository.save(p);
			return ResponseEntity.ok(new IdResponse(p.getId()));
		}catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new ErrorView(e.getMessage()));
		}
	}

	@PutMapping
	public ResponseEntity<?> update(@RequestBody ProductView product){
		try {
			Product p = new Product();
			modelMapper.map(product, p);
			productRepository.save(p);
			return ResponseEntity.ok(new IdResponse(p.getId()));
		}catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new ErrorView(e.getMessage()));
		}
	}

	@GetMapping("{id}/files/{idFile}")
	public ResponseEntity<?> getFiles(@PathVariable("id") Integer id, @PathVariable("idFile") Integer idFile){
		try {
			ProductFile pFile = productFileRepository.getById(idFile);
			
			BasicAWSCredentials awsCreds = new BasicAWSCredentials(s3AccessKey, s3Secret);
			AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_2)
				 .withCredentials(new AWSStaticCredentialsProvider(awsCreds)).build();
			S3Object file =  s3.getObject(s3Bucket, s3Folder + "/" + pFile.getName());
			byte[] data = IOUtil.readAllBytes(file.getObjectContent().getDelegateStream());
			return ResponseEntity.ok(data);
		}catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new ErrorView(e.getMessage()));
		}
	}

	@PostMapping("{id}/files")
	public ResponseEntity<?> insertFiles(@PathVariable("id") Integer id,  @RequestParam("file") MultipartFile file){
		try {
			Product p = productRepository.getById(id);
			ProductFile pf = new ProductFile();
			pf.setName(file.getOriginalFilename());
			pf.setProduct(p);
			productFileRepository.save(pf);
			
			BasicAWSCredentials awsCreds = new BasicAWSCredentials(s3AccessKey, s3Secret);
			AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_2)
				 .withCredentials(new AWSStaticCredentialsProvider(awsCreds)).build();
		    ObjectMetadata objectMetadata = new ObjectMetadata();
		    objectMetadata.setContentType(MediaTypeUtil.getMimetype(file.getOriginalFilename()));
			s3.putObject(s3Bucket, s3Folder + "/" + file.getOriginalFilename(), new ByteArrayInputStream(file.getBytes()), objectMetadata);
			return ResponseEntity.ok().build();
		}catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new ErrorView(e.getMessage()));
		}
	}
	
	@DeleteMapping("{id}/files/{idFile}")
	public ResponseEntity<?> deleteFile(@PathVariable("id") Integer id, @PathVariable("idFile") Integer idFile){
		try {
			ProductFile pFile = productFileRepository.getById(idFile);
			productFileRepository.delete(pFile);
			BasicAWSCredentials awsCreds = new BasicAWSCredentials(s3AccessKey, s3Secret);
			AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_2)
				 .withCredentials(new AWSStaticCredentialsProvider(awsCreds)).build();
			s3.deleteObject(s3Bucket, s3Folder + "/" + pFile.getName());
			return ResponseEntity.ok().build();
		}catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new ErrorView(e.getMessage()));
		}
	}
	
}
