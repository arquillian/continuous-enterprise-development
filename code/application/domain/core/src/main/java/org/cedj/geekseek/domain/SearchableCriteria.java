package org.cedj.geekseek.domain;

public interface SearchableCriteria {

    int getFirstResult();

    int getMaxResult();

    public static class PagedBase implements SearchableCriteria {

        private int pageSize = 10;
        private int pageNumber = 0;

        public int getPageNumber() {
            return this.pageNumber;
        }

        public int getPageSize() {
            return this.pageSize;
        }

        public void setPageNumber(int number) {
            this.pageNumber = number;
        }

        public void setPageSize(int size) {
            this.pageSize = size;
        }

        @Override
        public int getFirstResult() {
            return pageNumber == 0 ? 0:pageNumber*pageSize;
        }

        @Override
        public int getMaxResult() {
            return getFirstResult() + pageSize;
        }
    }
}
