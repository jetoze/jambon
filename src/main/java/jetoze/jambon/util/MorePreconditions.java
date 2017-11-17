package jetoze.jambon.util;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.base.Strings;

public final class MorePreconditions {
	public static int checkNotNegative(int n) {
		checkArgument(n >= 0);
		return n;
	}
	
	public static String checkNotEmpty(String s) {
		checkArgument(!Strings.isNullOrEmpty(s));
		return s;
	}
	
	private MorePreconditions() {/**/}

}
