import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import API from "../../../api/Api";


const initialState = {
    sources: []
}


export const getSourcesURLS = createAsyncThunk('endpointManagement/getSourcesURLS', async () => {
    return API.GET("getSourcesURLS", '', [] ).then(res => {
        const results = res.data
        return {results}
    })
})


const listSourcesURLS = createSlice({
    name: 'endpointManagement',
    initialState,
    reducers: {},
    extraReducers: {
        [getSourcesURLS.fulfilled]: (state, action) => {
            state.sources = action.payload.results
        },
    }
})


export const getListSourcesURLS = state => state.endpointManagement.sources


export default listSourcesURLS.reducer