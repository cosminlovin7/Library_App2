package com.paj2ee.library.config;

import com.paj2ee.library.model.LibAppBook;
import com.paj2ee.library.model.LibAppBookCollection;
import com.paj2ee.library.repository.LibAppBookCollectionRepository;
import com.paj2ee.library.repository.LibAppBookRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class AppDataConfigurer {

	@Autowired
	private LibAppBookRepository libAppBookRepository;

	@Autowired
	private LibAppBookCollectionRepository libAppBookCollectionRepository;

	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ)
	@PostConstruct
	public void initPostConstruct() {
		initBookData();
		initBookCollectionData();
	}

	private void initBookData() {
		LibAppBook book1 = new LibAppBook();
		book1.setTitle("The Whispering Woods");
		book1.setAuthor("Emily Hartwell");
		book1.setCoverType(LibAppBook.BookCoverType.CARTONATA);
		book1.setBookFormat("Print");
		book1.setCollection(null);
		book1.setEdition((short) 1);
		book1.setEditure("Greenleaf Publishing");
		book1.setIsbn("594-849-500-828-1");
		book1.setPageNr(312);
		book1.setTranslator("Élodie Laurent");
		book1.setYearOfPublication(2023);

		// Book 2
		LibAppBook book2 = new LibAppBook();
		book2.setTitle("A Journey Through Time");
		book2.setAuthor("Dr. Marcus Vance");
		book2.setCoverType(LibAppBook.BookCoverType.CARTONATA);
		book2.setBookFormat("Print");
		book2.setCollection(null);
		book2.setEdition((short) 3);
		book2.setEditure("Greenleaf Publishing");
		book2.setIsbn("594-849-500-829-2");
		book2.setPageNr(480);
		book2.setTranslator("Élodie Laurent");
		book2.setYearOfPublication(2021);

		// Book 3
		LibAppBook book3 = new LibAppBook();
		book3.setTitle("The Alchemist's Code");
		book3.setAuthor("Sophia Reyes");
		book3.setCoverType(LibAppBook.BookCoverType.CARTONATA);
		book3.setBookFormat("Print");
		book3.setCollection(null);
		book3.setEdition((short) 1);
		book3.setEditure("Greenleaf Publishing");
		book3.setIsbn("594-849-500-830-3");
		book3.setPageNr(256);
		book3.setTranslator("Élodie Laurent");
		book3.setYearOfPublication(2019);

		// Book 4
		LibAppBook book4 = new LibAppBook();
		book4.setTitle("Tales from the Horizon");
		book4.setAuthor("Jonathan Ellington");
		book4.setCoverType(LibAppBook.BookCoverType.CARTONATA);
		book4.setBookFormat("Digital (eBook)");
		book4.setCollection(null);
		book4.setEdition((short) 2);
		book4.setEditure("Greenleaf Publishing");
		book4.setIsbn("594-849-500-831-4");
		book4.setPageNr(350);
		book4.setTranslator("Élodie Laurent");
		book4.setYearOfPublication(2022);

		// Book 5
		LibAppBook book5 = new LibAppBook();
		book5.setTitle("The Lost Manuscripts");
		book5.setAuthor("Isabella Clarke");
		book5.setCoverType(LibAppBook.BookCoverType.CARTONATA);
		book5.setBookFormat("Digital (eBook)");
		book5.setCollection(null);
		book5.setEdition((short) 1);
		book5.setEditure("Greenleaf Publishing");
		book5.setIsbn("594-849-500-832-5");
		book5.setPageNr(672);
		book5.setTranslator("Élodie Laurent");
		book5.setYearOfPublication(2020);

		if (null == libAppBookRepository.findByTitleAndAuthorAndEditure(book1.getTitle(), book1.getAuthor(), book1.getEditure())) {
			libAppBookRepository.save(book1);
		}
		if (null == libAppBookRepository.findByTitleAndAuthorAndEditure(book2.getTitle(), book2.getAuthor(), book2.getEditure())) {
			libAppBookRepository.save(book2);
		}
		if (null == libAppBookRepository.findByTitleAndAuthorAndEditure(book3.getTitle(), book3.getAuthor(), book3.getEditure())) {
			libAppBookRepository.save(book3);
		}
		if (null == libAppBookRepository.findByTitleAndAuthorAndEditure(book4.getTitle(), book4.getAuthor(), book4.getEditure())) {
			libAppBookRepository.save(book4);
		}
		if (null == libAppBookRepository.findByTitleAndAuthorAndEditure(book5.getTitle(), book5.getAuthor(), book5.getEditure())) {
			libAppBookRepository.save(book5);
		}
	}

	private void initBookCollectionData() {
		LibAppBookCollection bookCollection1 = new LibAppBookCollection();
		bookCollection1.setName("Classics Collection");

		LibAppBookCollection bookCollection2 = new LibAppBookCollection();
		bookCollection2.setName("Bestsellers of the Decade");

		LibAppBookCollection bookCollection3 = new LibAppBookCollection();
		bookCollection3.setName("Mystery & Thrillers");

		LibAppBookCollection bookCollection4 = new LibAppBookCollection();
		bookCollection4.setName("Historical Chronicles");

		LibAppBookCollection bookCollection5 = new LibAppBookCollection();
		bookCollection5.setName("Science Fiction Adventures");

		LibAppBookCollection bookCollection6 = new LibAppBookCollection();
		bookCollection6.setName("Literary Masterpieces");

		LibAppBookCollection bookCollection7 = new LibAppBookCollection();
		bookCollection7.setName("Young Adult Reads");

		LibAppBookCollection bookCollection8 = new LibAppBookCollection();
		bookCollection8.setName("Non-Fiction Gems");

		LibAppBookCollection bookCollection9 = new LibAppBookCollection();
		bookCollection9.setName("Poetry Anthologies");

		if (null == libAppBookCollectionRepository.findByName(bookCollection1.getName())) {
			libAppBookCollectionRepository.save(bookCollection1);
		}
		if (null == libAppBookCollectionRepository.findByName(bookCollection2.getName())) {
			libAppBookCollectionRepository.save(bookCollection2);
		}
		if (null == libAppBookCollectionRepository.findByName(bookCollection3.getName())) {
			libAppBookCollectionRepository.save(bookCollection3);
		}
		if (null == libAppBookCollectionRepository.findByName(bookCollection4.getName())) {
			libAppBookCollectionRepository.save(bookCollection4);
		}
		if (null == libAppBookCollectionRepository.findByName(bookCollection5.getName())) {
			libAppBookCollectionRepository.save(bookCollection5);
		}
		if (null == libAppBookCollectionRepository.findByName(bookCollection6.getName())) {
			libAppBookCollectionRepository.save(bookCollection6);
		}
		if (null == libAppBookCollectionRepository.findByName(bookCollection7.getName())) {
			libAppBookCollectionRepository.save(bookCollection7);
		}
		if (null == libAppBookCollectionRepository.findByName(bookCollection8.getName())) {
			libAppBookCollectionRepository.save(bookCollection8);
		}
		if (null == libAppBookCollectionRepository.findByName(bookCollection9.getName())) {
			libAppBookCollectionRepository.save(bookCollection9);
		}

	}
}
