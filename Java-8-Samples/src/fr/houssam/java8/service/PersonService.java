package fr.houssam.java8.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import fr.houssam.java8.entity.Person;

public class PersonService {
	public List<Person> getPersonWithAgeGreaterThan10(List<Person> persons){
		return persons.stream()
				.filter(person -> person.getAge() > 10)
				.collect(Collectors.toList());
	}

	public int getPersonAgesSum(List<Person> persons){
		return persons.stream()
				.mapToInt(Person::getAge)
				.distinct()
				.sum();
	}

	public void printPersonWithAgeGreaterThan10(List<Person> persons){
		persons.forEach(
				Person::toString
				);
	}

	//flatMap et groupingBy counting
	public Map<String,Long> countByActivity(List<Person> persons){
		return persons.stream()
				.flatMap(p->p.getActivite().stream())
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
	}
	
	//flatMap sample
	public List<String> getAllActivities(List<Person> persons){
		return persons.stream()
				.flatMap(p->p.getActivite().stream())
				.collect(Collectors.toList());
	}
	
	//distinct et count
	public long countAges(List<Person> persons){
		return persons.stream()
			.map(Person::getAge)
			.distinct()
			.count();
	}
	
	//peek method (sert a debugger)
	public List<Person> peek(List<Person> persons){
		return persons.stream()
		.filter(p -> p.getAge() > 34)
		.peek(p -> System.out.println(p.getNom()))
		.collect(Collectors.toList());
	}
	
	public void toto(List<Person> persons){
		Function<Person, Integer> mapper = person -> person.getId();
		persons.stream()
		.map(mapper)
		.collect(Collectors.toMap(v->v, h->h));
	}

	public List<Person> getByActivity(List<Person> persons, String activite){
		List<Person> personsResult = Lists.newArrayList(); 
		persons.stream()
		.forEach(
				p->{
					for (String act: p.getActivite()) {
						if(activite.equals(act)){
							personsResult.add(p);
						}
					}
				}
				);
		return personsResult;
	}
	
	/****************Filtre and Predicate*****************/
	//Filtrer avec person -> person.getId() == id.
	public Optional<Person> fromPersonsByIdFacon1(int id, List<Person> persons){
		return persons.stream()
				.filter(person -> person.getId() == id)
				.findAny();
	}
	
	//Filtrer avec l'implémentation de la méthode test de l'interface Predicate.
	public Optional<Person> fromPersonsByIdFacon2(int id, List<Person> persons){
		return persons.stream()
				.filter(new Predicate<Person>() {
					public boolean test(Person t) {
						return t.getId() == id;
					}
				})
				.findAny();
	}
	
	//Filter avec un objet Predicate.
	public Optional<Person> fromPersonByIdFacon3 (int id, List<Person> persons){
		Predicate<Person> predicate = p -> p.getId() == id;
		return persons.stream()
				.filter(predicate)
				.findAny();
	}
	
	/****************Foreach and Consumer***************/

	public void consumerMethode1(List<Person> persons){
		persons
			.stream()
			.forEach(Person::getNom);
	}
	
	public void consumerMethodeFacon2(List<Person> persons){
		persons
			.stream()
			.forEach(new Consumer<Person>() {
				public void accept(Person person) {
					System.out.println("Hello "+person.getNom());
				}
			});
	}
	
	public void consumerMethode3(List<Person> persons){
		Consumer<Person> consumer = person -> System.out.println("Hello "+person.getNom());
		persons
			.stream()
			.forEach(consumer);
	}
	
	/********Mapper and map********/
	
	public void mapperMethodeFacon1(List<Person> persons){
		persons
			.stream()
			.map(new Function<Person, String>() {
				public String apply(Person person) {
					return person.getNom();
				}
			});
	}
	
	public void mapperMethodeFacon2(List<Person> persons){
		persons
			.stream()
			.map(person -> person.getNom());
	}
	
	public void mapperMethodeFacon3(List<Person> persons){
		Function<Person, String> mapper = (Person person) -> person.getNom();
		persons
			.stream()
			.map(mapper);
	}
	
	/**************Supplier**************/
	
	public void supplier(){
		Supplier<Person> supplier1 =  Person::new;
		
		Supplier<Person> supplier2 = () -> new Person();
		
		Supplier<Person> supplier3 = PersonService::forSupplier;
		
		if(supplier1.get().equals(supplier2.get())){
			System.out.println("1 et 2 sont égaux");
		}
		
		if(supplier3.get().equals(supplier1.get())){
			System.out.println("1 et 3 sont égaux");
		}
		
	}
	
	public static Person forSupplier(){
		return new Person();
	}

	public <T> void doSomething(Supplier<T> function, Consumer<T> onSuccess,
			Consumer<Exception> onError) {
		try {
			T res = function.get();
			onSuccess.accept(res);
			throw new Exception();
		} catch (Exception ex) {
			onError.accept(ex);
		}
	}
}
