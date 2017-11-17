package jetoze.jambon.util;

import java.util.Collections;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

public final class ImCollectors {

	public static <T> Collector<T, ?, ImmutableList<T>> toList() {
		return new ImListCollector<>();
	}

	public static <T> Collector<T, ?, ImmutableSet<T>> toSet() {
		return new ImSetCollector<>();
	}
	
	
	private static class ImListCollector<T> implements Collector<T, ImmutableList.Builder<T>, ImmutableList<T>> {

		@Override
		public Supplier<ImmutableList.Builder<T>> supplier() {
			return ImmutableList.Builder::new;
		}

		@Override
		public BiConsumer<ImmutableList.Builder<T>, T> accumulator() {
			return (b, e) -> b.add(e);
		}

		@Override
		public BinaryOperator<ImmutableList.Builder<T>> combiner() {
			// TODO Auto-generated method stub
			return (b1, b2) -> b1.addAll(b2.build());
		}

		@Override
		public Function<ImmutableList.Builder<T>, ImmutableList<T>> finisher() {
			return ImmutableList.Builder::build;
		}

		@Override
		public Set<Characteristics> characteristics() {
			return Collections.emptySet();
		}
	}
	
	
	private static class ImSetCollector<T> implements Collector<T, ImmutableSet.Builder<T>, ImmutableSet<T>> {

		@Override
		public Supplier<ImmutableSet.Builder<T>> supplier() {
			return ImmutableSet.Builder::new;
		}

		@Override
		public BiConsumer<ImmutableSet.Builder<T>, T> accumulator() {
			return (b, e) -> b.add(e);
		}

		@Override
		public BinaryOperator<ImmutableSet.Builder<T>> combiner() {
			// TODO Auto-generated method stub
			return (b1, b2) -> b1.addAll(b2.build());
		}

		@Override
		public Function<ImmutableSet.Builder<T>, ImmutableSet<T>> finisher() {
			return ImmutableSet.Builder::build;
		}

		@Override
		public Set<Characteristics> characteristics() {
			return Collections.emptySet();
		}
	}
	
	private ImCollectors() {/**/}

}
