import { useEffect, useRef } from 'react';

export const usePolling = (
    callback: () => void | Promise<void>,
    interval: number,
    enabled: boolean = true
) => {
    const savedCallback = useRef(callback);
    const intervalRef = useRef<number | null>(null);

    useEffect(() => {
        savedCallback.current = callback;
    }, [callback]);

    useEffect(() => {
        if (!enabled) return;

        const tick = async () => {
            await savedCallback.current();
        };

        tick();
        if (interval > 0) {
            intervalRef.current = setInterval(tick, interval);
        }

        return () => {
            if (intervalRef.current) {
                clearInterval(intervalRef.current);
            }
        };
    }, [interval, enabled]);
};