import { configureStore } from '@reduxjs/toolkit'
import searchReducer from '../features/search/searchSlice'

export default configureStore({
    reducer: {
        search: searchReducer
    }
})