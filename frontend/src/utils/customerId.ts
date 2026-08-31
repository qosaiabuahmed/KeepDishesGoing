const CUSTOMER_ID_KEY = 'kdg_customer_id';

export const getCustomerId = (): string => {
    let customerId = localStorage.getItem(CUSTOMER_ID_KEY);

    if (!customerId) {
        customerId = generateUUID();
        localStorage.setItem(CUSTOMER_ID_KEY, customerId);
    }

    return customerId;
};

export const clearCustomerId = (): void => {
    localStorage.removeItem(CUSTOMER_ID_KEY);
};

const generateUUID = (): string => {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, (c) => {
        const r = Math.random() * 16 | 0;
        const v = c === 'x' ? r : (r & 0x3 | 0x8);
        return v.toString(16);
    });
};