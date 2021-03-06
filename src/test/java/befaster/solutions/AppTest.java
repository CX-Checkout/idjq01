package befaster.solutions;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.equalTo;

import org.junit.Test;

public class AppTest {

	@Test
	public void a_costs_fiddy() {
		assertThat(App.checkout("A"), equalTo(50));
	}

	@Test
	public void b_costs_thirty() {
		assertThat(App.checkout("B"), equalTo(30));
	}
	
	@Test
	public void c_costs_twenty() {
		assertThat(App.checkout("C"), equalTo(20));
	}
	
	@Test
	public void d_costs_fifteen() {
		assertThat(App.checkout("D"), equalTo(15));
	}
	
	@Test
	public void three_a_costs_130() {
		assertThat(App.checkout("AAA"), equalTo(130));
	}
	
	@Test
	public void two_b_costs_45() {
		assertThat(App.checkout("BB"), equalTo(45));
	}
	
	@Test
	public void four_a_costs_130_plus_fifty() {
		assertThat(App.checkout("AAAA"), equalTo(180));
	}
	
	@Test
	public void can_have_multiple() {
		assertThat(App.checkout("BBAAAABC"), equalTo(180 + 45 + 30 + 20));
	}
	
	@Test
	public void empty_string_gives_zero() {
		assertThat(App.checkout(""), equalTo(0));
	}
	
	@Test
	public void invalid_letter_gives_minus1() {
		assertThat(App.checkout("1"), equalTo(-1));
	}
	
	@Test
	public void e_costs_forty() {
		assertThat(App.checkout("E"), equalTo(40));
	}
	
	@Test
	public void five_a_costs_200() {
		assertThat(App.checkout("AAAAA"), equalTo(200));
	}
	
	@Test
	public void multi_multibuy_works_correctly() {
		assertThat(App.checkout("AAAAAAAAA"), equalTo(200 + 180));
	}
	
	@Test
	public void two_e_give_one_free_b() {
		assertThat(App.checkout("EBE"), equalTo(80));
	}
	
	@Test
	public void two_e_give_one_free_b_multi() {
		assertThat(App.checkout("EBEBEE"), equalTo(160));
	}
	
	@Test
	public void ABCDE_155() {
		assertThat(App.checkout("ABCDE"), equalTo(155));
	}
	
	@Test
	public void ABCDEABCDE_280() {
		assertThat(App.checkout("ABCDEABCDE"), equalTo(280));
	}
	
	@Test
	public void CCADDEEBBA_280() {
		assertThat(App.checkout("CCADDEEBBA"), equalTo(280));
	}
	
	@Test
	public void f_costs_10() {
		assertThat(App.checkout("F"), equalTo(10));
	}
	
	@Test
	public void FFF_costs_20() {
		assertThat(App.checkout("FFF"), equalTo(20));
	}
	
	@Test
	public void FFFFFEBE_costs_110() {
		assertThat(App.checkout("FFFFEBE"), equalTo(80 + 30));
	}
	
	@Test
	public void FFFFF_costs_30() {
		assertThat(App.checkout("FFFF"), equalTo(30));
	}
	
	@Test
	public void test_10H_and_4R_and_2Q_is_something() {
		assertThat(App.checkout("HHHHHHHHHHRRRRQQ"), equalTo(80 + 200 + 30));
	}
	
	@Test
	public void test_10H_and_4R_and_4Q_is_something() {
		assertThat(App.checkout("HHHHHHHHHHRRRRQQQQ"), equalTo(80 + 200 + 80));
	}
	
	@Test
	public void test_something_evil() {
		assertThat(App.checkout("SSSTTTXXXYYYZZZ"), equalTo(5 * 45));
	}
	
	@Test
	public void picks_most_expensive() {
		assertThat(App.checkout("SZZZS"), equalTo(45 + 40));
	}
}
