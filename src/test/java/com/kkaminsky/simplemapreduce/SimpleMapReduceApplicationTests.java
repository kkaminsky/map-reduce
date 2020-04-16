package com.kkaminsky.simplemapreduce;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class SimpleMapReduceApplicationTests {


	@Test
	public void test1(){
		StringBuilder str =  new StringBuilder();
		for(int i = 0; i<1000; i++){
			str.append("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. ");
		}
		List<String> a = Flux.just(str.toString())
				.map(String::toUpperCase)

				.flatMap(s -> Flux.fromArray(s.split(" ")))

				.groupBy(String::toString,Integer.MAX_VALUE)

				.sort(Comparator.comparing(GroupedFlux::key))

				.delayElements(Duration.ofMillis(1))

				.flatMap(group -> Mono.zip(Mono.just(group.key()), group.count()))

				.map(keyAndCount -> keyAndCount.getT1() + " => " + keyAndCount.getT2() + "; ").collectList().block();

		for(String s: a){
			System.out.println(s);
		}
	}

	@Test
	public void test2() throws InterruptedException {
		StringBuilder str =  new StringBuilder();
		for(int i = 0; i<1000; i++){

			str.append("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. ");

		}

		Map<String, Integer> hashMap = new HashMap<>();

		for(String word:str.toString().split(" ")){
			if(hashMap.containsKey(word)){
				hashMap.put(word,hashMap.get(word) + 1);
			}else{
				hashMap.put(word,1);
			}
			Thread.sleep(1);
		}

	}

}
