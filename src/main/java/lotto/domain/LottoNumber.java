package lotto.domain;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lotto.exceptions.LottoNumberRangeException;

public class LottoNumber {
	private static final int MAX_LOTTO_NUMBER_RANGE = 45;
	private static final int MIN_LOTTO_NUMBER_RANGE = 1;
	private static final String LOTTO_NUMBER_RANGE_MESSAGE = "1~45 범위의 숫자만 로또 번호가 될 수 있습니다.";

	private static final Map<Integer, LottoNumber> lottoNumberFactory =
		IntStream.rangeClosed(MIN_LOTTO_NUMBER_RANGE, MAX_LOTTO_NUMBER_RANGE)
			.mapToObj(LottoNumber::new)
			.collect(Collectors.toMap(LottoNumber::getNumber, x -> x));

	private int lottoNumber;

	private LottoNumber(int lottoNumber) {
		this.lottoNumber = lottoNumber;
	}

	public static LottoNumber of(int lottoNumber) {
		if (isInvalidNumberRange(lottoNumber)) {
			throw new LottoNumberRangeException(LOTTO_NUMBER_RANGE_MESSAGE);
		}

		return lottoNumberFactory.get(lottoNumber);
	}

	private static boolean isInvalidNumberRange(int number) {
		return number > MAX_LOTTO_NUMBER_RANGE || number < MIN_LOTTO_NUMBER_RANGE;
	}

	public int getNumber() {
		return lottoNumber;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		LottoNumber that = (LottoNumber)o;
		return lottoNumber == that.lottoNumber;
	}

	@Override
	public int hashCode() {
		return Objects.hash(lottoNumber);
	}
}
