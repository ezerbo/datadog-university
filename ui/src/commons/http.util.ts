import {AxiosResponse} from "axios";

export const handleHttpErrors = (response: AxiosResponse) => {
    if (response.status !== 200) {
        throw response;
    }
    return response;
}

export const handleErrors = (response: AxiosResponse) => {
    if (response.status !== 200) {
        console.error(`${JSON.stringify(response)}`);
        throw response;
    }
    return response;
}

export const formatErrors = (error: any) => {
    return error.response.data.fieldErrors.map(fieldError => `${fieldError.field}: ${fieldError.message}.`);
}

export const toDate = (date: any) => {
    return new Date(date).toLocaleString(undefined,
        {
            day: 'numeric',
            month: 'numeric',
            year: 'numeric',
        }
    );
}