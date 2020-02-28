package lotto.view;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import lotto.domain.LottoAmount;
import lotto.domain.Lottos;
import lotto.domain.Money;
import lotto.domain.Rank;
import lotto.domain.Ranks;

public class OutputView {
	private static final int PERCENTAGE_MULTIPLE = 100;
	private static final String EMPTY_STRING = "";
	private static final String EARNING_RATE_MESSAGE = "총 수익률은 %d %%입니다.";
	private static final String SECOND_RANK_ADDITIONAL_MESSAGE = "보너스 볼 일치 ";
	private static final String PURCHASED_LOTTO_MESSAGE = "수동으로 %d개, 자동으로 %d를 구매했습니다.";
	private static final String NEW_LINE = "\n";
	private static final String STATISTICS_FORMAT = "%d개 일치 %s%s원 - %d개%s";

	public static void showEarningRate(Money boughtLottoMoney, Ranks ranks) {
		long earningRate = ranks.stream()
			.filter(Objects::nonNull)
			.map(Rank::getReward)
			.reduce(Money::sum)
			.map(sum -> sum.multiple(PERCENTAGE_MULTIPLE))
			.map(sum -> sum.getQuotient(boughtLottoMoney))
			.orElse(0L);

		System.out.printf(EARNING_RATE_MESSAGE, earningRate);
	}

	public static void showStatistics(Ranks ranks) {
		Map<Rank, Long> rankCounts = ranks.stream()
			.filter(Objects::nonNull)
			.collect(Collectors.groupingBy(x -> x, Collectors.counting()));

		for (Rank rank : Rank.values()) {
			rankCounts.putIfAbsent(rank, 0L);
		}

		rankCounts.keySet().stream().sorted().forEach(rank ->
			System.out.printf(STATISTICS_FORMAT, rank.getHitCount(), printBonus(rank), rank.getReward(),
				rankCounts.get(rank), NEW_LINE));
	}

	public static String printBonus(Rank rank) {
		if (rank == Rank.SECOND) {
			return SECOND_RANK_ADDITIONAL_MESSAGE;
		}

		return EMPTY_STRING;
	}

	public static void showPurchasedLottoCount(LottoAmount purchasedLottoAmount) {
		System.out.printf(PURCHASED_LOTTO_MESSAGE, purchasedLottoAmount.getManualLottoAmount(),
			purchasedLottoAmount.getAutoLottoAmount());
	}

	public static void showPurchasedLottoNumbers(Lottos purchasedLottoNumbers) {
		System.out.println(purchasedLottoNumbers.stream()
			.map(x -> String.valueOf(x.getNumbers()))
			.collect(Collectors.joining(NEW_LINE))
		);
	}

	public static void showResult(Money money, Ranks ranks) {
		showStatistics(ranks);
		showEarningRate(money, ranks);
	}
}
