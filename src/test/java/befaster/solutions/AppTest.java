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
}
