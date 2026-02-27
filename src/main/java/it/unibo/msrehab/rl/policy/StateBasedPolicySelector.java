package it.unibo.msrehab.rl.policy;

import it.unibo.msrehab.rl.utils.AbstractBuilder;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;


/**
 * Policy that decides which sub-policy to use depending on visited states
 * @param <S>
 * @param <A>
 */
public class StateBasedPolicySelector<S, A> implements IActionPolicy<S, A>
{
    public static class Builder<S, A> extends AbstractBuilder<StateBasedPolicySelector<S, A>>
    {
        public Builder<S, A> addPolicy(Predicate<S> predicate, IActionPolicy<S, A> policy)
        {
            return setValue(this, a -> a.policies.put(predicate, policy));
        }

        public Builder<S, A> setDefaultPolicy(IActionPolicy<S, A> policy)
        {
            return setValue(this, a -> a.defaultPolicy = policy);
        }

        @Override
        protected StateBasedPolicySelector<S, A> newInstance()
        {
            return new StateBasedPolicySelector<>();
        }
    }

    private final Map<Predicate<S>, IActionPolicy<S, A>> policies = new LinkedHashMap<>();

    private IActionPolicy<S, A> defaultPolicy = s -> null;

    private IActionPolicy<S, A> currentPolicy;

    public StateBasedPolicySelector()
    {
        currentPolicy = defaultPolicy;
    }

    public void reset()
    {
        currentPolicy = defaultPolicy;
    }

    private void selectPolicy(S state)
    {
        currentPolicy = policies.entrySet()
                .stream()
                .filter(e -> e.getKey().test(state))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElse(currentPolicy);
    }


    @Override
    public A selectAction(S state)
    {
        selectPolicy(state);
        return currentPolicy.selectAction(state);
    }
}
