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
		assertThat(App.checkout("G"), equalTo(-1));
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
}
