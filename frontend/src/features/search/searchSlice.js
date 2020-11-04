import { createAsyncThunk, createSlice } from '@reduxjs/toolkit'
import API from '../../api/Api'


const initialState = {
    results:[],
    query: "",
    status: 'idle',
    error: null
}

export const getResults = createAsyncThunk('search/getResults', async (searchInput) => {
    return API.GET("searchResults", "id", [searchInput] ).then(res => {
        return res.data.results.length > 0 ? res.data.results : []
    })
})

export const getAutocomplete = createAsyncThunk('search/autocomplete', async (searchInput) => {
    return API.GET("searchAutocomplete", "", [searchInput] ).then(res => {
        console.log(res.data)
        return res.data;
    })
})

const searchSlice = createSlice({
    name: 'search',
    initialState,
    reducers: {},
    extraReducers: {
        [getResults.pending]: (state, action) => {
            state.status = 'loading'
            state.query = action.meta.arg
        },
        [getResults.fulfilled]: (state, action) => {
            state.status = 'succeeded'
            state.results = action.payload
            state.query = action.meta.arg
        },
        [getResults.rejected]: (state, action) => {
            state.status = 'failed'
            state.query = action.meta.arg
            state.error = action.error.message
        },

        [getAutocomplete.pending]: (state, action) => {
            state.status = 'loading'
            state.query = action.meta.arg
        },
        [getAutocomplete.fulfilled]: (state, action) => {
            state.status = 'succeeded'
            state.results = action.payload
            state.query = action.meta.arg
        },
        [getAutocomplete.rejected]: (state, action) => {
            state.status = 'failed'
            state.query = action.meta.arg
            state.error = action.error.message
        }
    }
})

export const selectAllResults = state => state.search.results
export const selectQuery = state => state.search.query
export const getNumberOfResults = state => state.search.results.length === 0 ? 0 : state.search.results.length
export const getStatus = state => state.search.status

export default searchSlice.reducer