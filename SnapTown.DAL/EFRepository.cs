using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Data.SqlClient;
using System.Linq;
using System.Linq.Expressions;
using SnapTown.Models;

namespace SnapTown.DAL
{
    public class EFRepository<T> : IRepository<T> where T : class
    {
        private SnapTownDbContext dbContext;

        public EFRepository(SnapTownDbContext dbContext)
        {
            this.dbContext = dbContext;

            //SERIALIZE WILL FAIL WITH PROXIED ENTITIES
            dbContext.Configuration.ProxyCreationEnabled = false;

            //ENABLING COULD CAUSE ENDLESS LOOPS AND PERFORMANCE PROBLEMS
            dbContext.Configuration.LazyLoadingEnabled = false;
        }

        public IQueryable<T> All(string[] includes = null)
        {
            //HANDLE INCLUDES FOR ASSOCIATED OBJECTS IF APPLICABLE
            if (includes != null && includes.Count() > 0)
            {
                var query = dbContext.Set<T>().Include(includes.First());
                foreach (var include in includes.Skip(1))
                    query = query.Include(include);
                return query.AsQueryable();
            }

            return dbContext.Set<T>().AsQueryable();
        }

        public T Get(Expression<Func<T, bool>> expression, string[] includes = null)
        {
            return All(includes).FirstOrDefault(expression);
        }

        public virtual T Find(Expression<Func<T, bool>> predicate, string[] includes = null)
        {
            //HANDLE INCLUDES FOR ASSOCIATED OBJECTS IF APPLICABLE
            if (includes != null && includes.Count() > 0)
            {
                var query = dbContext.Set<T>().Include(includes.First());
                foreach (var include in includes.Skip(1))
                    query = query.Include(include);
                return query.FirstOrDefault<T>(predicate);
            }

            return dbContext.Set<T>().FirstOrDefault<T>(predicate);
        }

        public virtual IQueryable<T> Filter(Expression<Func<T, bool>> predicate, string[] includes = null)
        {
            //HANDLE INCLUDES FOR ASSOCIATED OBJECTS IF APPLICABLE
            if (includes != null && includes.Count() > 0)
            {
                var query = dbContext.Set<T>().Include(includes.First());
                foreach (var include in includes.Skip(1))
                    query = query.Include(include);
                return query.Where<T>(predicate).AsQueryable<T>();
            }

            return dbContext.Set<T>().Where<T>(predicate).AsQueryable<T>();
        }

        public virtual IQueryable<T> Filter(Expression<Func<T, bool>> predicate,
            out int total, int index = 0, int size = 50, string[] includes = null)
        {
            int skipCount = index * size;
            IQueryable<T> _resetSet;

            //HANDLE INCLUDES FOR ASSOCIATED OBJECTS IF APPLICABLE
            if (includes != null && includes.Count() > 0)
            {
                var query = dbContext.Set<T>().Include(includes.First());
                foreach (var include in includes.Skip(1))
                    query = query.Include(include);
                _resetSet = predicate != null ? query.Where<T>(predicate).AsQueryable() : query.AsQueryable();
            }
            else
            {
                _resetSet = predicate != null ? dbContext.Set<T>().Where<T>(predicate).AsQueryable() : dbContext.Set<T>().AsQueryable();
            }

            _resetSet = skipCount == 0 ? _resetSet.Take(size) : _resetSet.Skip(skipCount).Take(size);
            total = _resetSet.Count();
            return _resetSet.AsQueryable();
        }

        public virtual T Create(T TObject)
        {
            var newEntry = dbContext.Set<T>().Add(TObject);
            dbContext.SaveChanges();
            return newEntry;
        }

        public virtual int Delete(T TObject)
        {
            dbContext.Set<T>().Remove(TObject);
            return dbContext.SaveChanges();
        }

        public virtual int Update(T TObject)
        {
            var entry = dbContext.Entry(TObject);
            dbContext.Set<T>().Attach(TObject);
            entry.State = EntityState.Modified;
            return dbContext.SaveChanges();
        }

        public virtual int Delete(Expression<Func<T, bool>> predicate)
        {
            var objects = Filter(predicate);
            foreach (var obj in objects)
                dbContext.Set<T>().Remove(obj);
            return dbContext.SaveChanges();
        }

        public bool Contains(Expression<Func<T, bool>> predicate)
        {
            return dbContext.Set<T>().Count<T>(predicate) > 0;
        }

        public virtual void ExecuteProcedure(String procedureCommand, params SqlParameter[] sqlParams)
        {
            dbContext.Database.ExecuteSqlCommand(procedureCommand, sqlParams);

        }

        public void Dispose()
        {
            if (dbContext != null)
                dbContext.Dispose();
        }

    }
}
